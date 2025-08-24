package sh.lmao.event_hub.services;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import sh.lmao.event_hub.dto.mappers.ActivityMapper;
import sh.lmao.event_hub.dto.response.ActivityInstanceDTO;
import sh.lmao.event_hub.entities.Activity;
import sh.lmao.event_hub.entities.ActivityInstance;
import sh.lmao.event_hub.entities.Participant;
import sh.lmao.event_hub.exceptions.AlreadyExistsException;
import sh.lmao.event_hub.exceptions.NotFoundException;
import sh.lmao.event_hub.repositories.ActivityRepo;
import sh.lmao.event_hub.repositories.ActivityInstanceRepo;
import sh.lmao.event_hub.repositories.ParticipantRepo;

@Service
public class ActivityInstanceService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityInstanceService.class);

    @Autowired
    private ActivityRepo activityRepo;

    @Autowired
    private ActivityInstanceRepo activityInstanceRepo;

    @Autowired
    private ParticipantRepo participantRepo;

    @Autowired
    private ActivityMapper activityMapper;

    public ActivityInstance createInstance(Activity activity, ZonedDateTime eventDate) {
        ActivityInstance instance = new ActivityInstance();
        instance.setActivity(activity);
        instance.setEventDate(eventDate);
        return activityInstanceRepo.save(instance);
    }

    @Scheduled(fixedRate = 7, timeUnit = TimeUnit.DAYS)
    private void scheduledFutureInstancePopulator() {
        for (Activity activity : activityRepo.findAll()) {
            initFuturePopulating(activity);
        }
    }

    public void initFuturePopulating(Activity activity) {
        // if the repeat interval is zero, we don't want to repeat the activity
        if (activity.getRepeatInterval() == 0)
            return;

        int months = 3;
        int daysToPopulateInFuture = months * 30;
        if (populateFutureInstances(activity, daysToPopulateInFuture).size() == 0) {
            logger.warn("tried to populate activity instances for '" + activity.getName() + "'" + " '"
                    + daysToPopulateInFuture
                    + "' days ahead, but no instances were created");
        }
    }

    private List<ActivityInstance> populateFutureInstances(Activity activity, int days) {
        List<ActivityInstance> instances = new ArrayList<>();

        ZonedDateTime eventDate = activity.getEventDate();
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.systemDefault());
        int interval = activity.getRepeatInterval();

        long deltaAsDays = (now.toEpochSecond() - eventDate.toEpochSecond()) / 60 / 60 / 24;
        now = now.plusDays(days - (deltaAsDays % interval));

        while (now.toEpochSecond() > eventDate.toEpochSecond()) {
            if (activityInstanceRepo.findByEventDate(now).isPresent()) {
                break;
            }
            instances.add(createInstance(activity, now));
            now = now.plusDays(-interval);
        }

        return instances;
    }

    public Participant addParticipantForActivity(UUID activityInstanceId, Participant participant)
            throws NotFoundException {
        Optional<ActivityInstance> activity = activityInstanceRepo.findById(activityInstanceId);
        if (activity.isEmpty()) {
            throw new NotFoundException("no activity instance found with ID: " + activityInstanceId);
        }

        participant.setActivityInstance(activity.get());
        return participantRepo.save(participant);
    }

    public List<Participant> getAllParticipantsForActivity(UUID activityId) {
        return participantRepo.findByActivityId(activityId);
    }

    public List<ActivityInstance> getAllInstancesForActivity(Activity activity) {
        return activityInstanceRepo.findByActivityOrderByEventDate(activity);
    }

    public List<ActivityInstance> getAllNextActiveInstances() {
        List<ActivityInstance> instances = new ArrayList<>();
        List<Activity> activeActivities = activityRepo.findAllByActive(true);

        ZonedDateTime now = ZonedDateTime.now();
        for (Activity activity : activeActivities) {
            Optional<ActivityInstance> instance = activityInstanceRepo
                    .findFirstByActivityAndEventDateAfterOrderByEventDate(activity, now);
            instance.ifPresent(instances::add);
        }

        return instances;
    }

    // this is not very DRY
    public List<ActivityInstanceDTO> getAllNextActiveInstancesDTO() {
        List<ActivityInstanceDTO> dtos = new ArrayList<>();
        List<Activity> activeActivities = activityRepo.findAllByActive(true);

        ZonedDateTime now = ZonedDateTime.now();
        for (Activity activity : activeActivities) {
            Optional<ActivityInstance> instance = activityInstanceRepo
                    .findFirstByActivityAndEventDateAfterOrderByEventDate(activity, now);
            if (instance.isPresent()) {
                List<Participant> participants = participantRepo.findByActivityId(instance.get().getId());
                dtos.add(activityMapper.toDashboardInstanceDto(activity, instance.get(), participants.size()));
            }
        }

        return dtos;
    }
}
