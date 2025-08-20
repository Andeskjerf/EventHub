package sh.lmao.event_hub.services;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sh.lmao.event_hub.entities.Activity;
import sh.lmao.event_hub.entities.ActivityInstance;
import sh.lmao.event_hub.entities.Participant;
import sh.lmao.event_hub.exceptions.AlreadyExistsException;
import sh.lmao.event_hub.exceptions.NotFoundException;
import sh.lmao.event_hub.repositories.ActivityRepo;
import sh.lmao.event_hub.repositories.ActivityInstanceRepo;
import sh.lmao.event_hub.repositories.ParticipantRepo;

@Service
public class ActivityService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

    @Autowired
    private ActivityRepo activityRepo;

    @Autowired
    private ActivityInstanceRepo activityInstanceRepo;

    @Autowired
    private ParticipantRepo participantRepo;

    public Activity createActivity(Activity activity) throws AlreadyExistsException {
        if (activityRepo.findByName(activity.getName()).isPresent()) {
            throw new AlreadyExistsException("activity exists");
        }

        Activity savedActivity = activityRepo.save(activity);

        // there will always be at least one instance at the initial date
        createInstance(savedActivity, savedActivity.getEventDate());

        // if the interval is not 0, it should be repeated
        if (savedActivity.getRepeatInterval() != 0) {
            int daysToPopulateInFuture = 3 * 30;
            if (populateFutureInstances(savedActivity, daysToPopulateInFuture).size() == 0) {
                logger.warn("tried to populate activity instances for '" + savedActivity.getName() + "'"
                        + daysToPopulateInFuture
                        + "' days ahead, but no instances were created");
            }
        }

        return savedActivity;
    }

    private List<ActivityInstance> populateFutureInstances(Activity activity, int days) {
        List<ActivityInstance> instances = new ArrayList<>();

        ZonedDateTime eventDate = activity.getEventDate();
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.systemDefault());
        int interval = activity.getRepeatInterval();

        long deltaAsDays = (now.toEpochSecond() - eventDate.toEpochSecond()) / 60 / 60 / 24;
        now = now.plusDays(days - (deltaAsDays % interval));

        Optional<ActivityInstance> foundInstance = activityInstanceRepo.findByEventDate(now);
        while (foundInstance.isEmpty() && now.toEpochSecond() > eventDate.toEpochSecond()) {
            instances.add(createInstance(activity, now));
            now = now.plusDays(-interval);
        }

        return instances;
    }

    // FIXME: hmm... should this be in the activity service? would the "java spring
    // way" have its own ActivityInstanceService?
    private ActivityInstance createInstance(Activity activity, ZonedDateTime eventDate) {
        ActivityInstance instance = new ActivityInstance();
        instance.setActivity(activity);
        instance.setEventDate(eventDate);
        return activityInstanceRepo.save(instance);

    }

    public Participant addParticipantForActivity(UUID activityId, Participant participant) throws NotFoundException {
        Optional<Activity> activity = activityRepo.findById(activityId);
        if (activity.isEmpty()) {
            throw new NotFoundException("no activity found with ID: " + activityId);
        }

        participant.setActivity(activity.get());
        return participantRepo.save(participant);
    }

    public List<Participant> getAllParticipantsForActivity(UUID activityId) {
        return participantRepo.findByActivityId(activityId);
    }

    public List<Activity> getAllActivities() {
        return activityRepo.findAll();
    }

}
