package sh.lmao.event_hub.services;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import sh.lmao.event_hub.dto.mappers.ActivityMapper;
import sh.lmao.event_hub.dto.mappers.ParticipantMapper;
import sh.lmao.event_hub.dto.request.RegisterParticipantDTO;
import sh.lmao.event_hub.dto.response.ActivityInstanceDTO;
import sh.lmao.event_hub.entities.Activity;
import sh.lmao.event_hub.entities.ActivityInstance;
import sh.lmao.event_hub.entities.ActivityOption;
import sh.lmao.event_hub.entities.Participant;
import sh.lmao.event_hub.entities.ParticipantActivityOption;
import sh.lmao.event_hub.exceptions.AlreadyExistsException;
import sh.lmao.event_hub.exceptions.NotFoundException;
import sh.lmao.event_hub.repositories.ActivityRepo;
import sh.lmao.event_hub.repositories.ParticipantActivityOptionsRepo;
import sh.lmao.event_hub.repositories.ActivityInstanceRepo;
import sh.lmao.event_hub.repositories.ActivityOptionRepo;
import sh.lmao.event_hub.repositories.ParticipantRepo;

@Service
public class ActivityInstanceService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityInstanceService.class);

    @Autowired
    private ParticipantActivityOptionService participantActivityOptionService;

    @Autowired
    private ActivityRepo activityRepo;

    @Autowired
    private ActivityInstanceRepo activityInstanceRepo;

    @Autowired
    private ActivityOptionRepo activityOptionRepo;

    @Autowired
    private ParticipantRepo participantRepo;

    @Autowired
    private ParticipantMapper participantMapper;

    public ActivityInstance createInstance(Activity activity, ZonedDateTime eventDate) {
        ActivityInstance instance = new ActivityInstance();
        instance.setActivity(activity);
        instance.setEventDate(eventDate);
        return activityInstanceRepo.save(instance);
    }

    public void deleteFutureInstances(UUID activityId) {
        activityInstanceRepo.deleteByEventDateAfter(ZonedDateTime.now());
    }

    public Optional<ActivityInstance> getInstance(UUID activityInstanceId) {
        return activityInstanceRepo.findById(activityInstanceId);
    }

    public Optional<ActivityInstance> getPreviousInstance(UUID activityInstanceId) {
        ActivityInstance instance = activityInstanceRepo.findById(activityInstanceId).orElseThrow();
        return activityInstanceRepo.findFirstByEventDateBeforeOrderByEventDateDesc(instance.getEventDate());
    }

    public Optional<ActivityInstance> getNextInstance(UUID activityInstanceId) {
        ActivityInstance instance = activityInstanceRepo.findById(activityInstanceId).orElseThrow();
        return activityInstanceRepo.findFirstByEventDateAfterOrderByEventDate(instance.getEventDate());
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

    public Participant addParticipantForActivity(UUID activityInstanceId, RegisterParticipantDTO dto)
            throws NotFoundException {
        Optional<ActivityInstance> instance = activityInstanceRepo.findById(activityInstanceId);
        if (instance.isEmpty()) {
            throw new NotFoundException("no activity instance found with ID: " + activityInstanceId);
        }

        Participant participant = participantMapper.fromRegisterParticipantDtoToParticipant(dto, instance.get());

        participant.setActivityInstance(instance.get());
        participant = participantRepo.save(participant);

        List<String> activityOptionIds = dto.getActivityOptionIds();
        for (String id : activityOptionIds) {
            Optional<ActivityOption> option = activityOptionRepo.findById(UUID.fromString(id));
            if (option.isEmpty()) {
                logger.warn("failed to find activity option with given ID, " + id + ". skipping");
                continue;
            }

            participantActivityOptionService.createEntity(
                    participant.getId(),
                    option.get().getId());
        }

        return participant;
    }

    public List<ActivityInstance> getAllInstancesForActivity(Activity activity) {
        return activityInstanceRepo.findByActivityOrderByEventDate(activity);
    }
}
