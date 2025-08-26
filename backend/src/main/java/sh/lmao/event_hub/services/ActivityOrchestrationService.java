package sh.lmao.event_hub.services;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import sh.lmao.event_hub.dto.mappers.ActivityMapper;
import sh.lmao.event_hub.dto.request.ActivityDTO;
import sh.lmao.event_hub.dto.request.CreateActivityDTO;
import sh.lmao.event_hub.dto.response.ActivityInstanceDTO;
import sh.lmao.event_hub.entities.Activity;
import sh.lmao.event_hub.entities.ActivityInstance;
import sh.lmao.event_hub.entities.Participant;
import sh.lmao.event_hub.exceptions.AlreadyExistsException;
import sh.lmao.event_hub.exceptions.NotFoundException;
import sh.lmao.event_hub.repositories.ActivityRepo;
import sh.lmao.event_hub.repositories.ActivityInstanceRepo;
import sh.lmao.event_hub.repositories.ParticipantRepo;

// FIXME: this service should only chain together other service method calls
// right now, it's doing direct repo calls
@Service
public class ActivityOrchestrationService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityOrchestrationService.class);

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityInstanceService activityInstanceService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ActivityOptionService activityOptionService;

    @Autowired
    private ActivityRepo activityRepo;

    @Autowired
    private ActivityInstanceRepo activityInstanceRepo;

    @Autowired
    private ParticipantRepo participantRepo;

    @Autowired
    private ActivityMapper activityMapper;

    public Activity createActivity(CreateActivityDTO createActivityDto) throws AlreadyExistsException {
        Activity activity = activityMapper.fromCreateActivityDtoToActivity(createActivityDto);
        Activity savedActivity = activityService.createActivity(activity);

        // there will always be at least one instance at the initial date
        activityInstanceService.createInstance(savedActivity, savedActivity.getEventDate());
        // ensure there are instances into the future
        activityInstanceService.initFuturePopulating(savedActivity);

        activityOptionService.createOptionsFromList(createActivityDto.getOptions(), activity);

        return savedActivity;
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

    public Optional<ActivityInstanceDTO> getActivityInstanceDTO(UUID activityInstanceId) {
        Optional<ActivityInstance> instance = activityInstanceService.getInstance(activityInstanceId);
        if (instance.isEmpty()) {
            return Optional.empty();
        }

        Optional<Activity> activity = activityService.getActivity(instance.get().getActivity().getId());
        if (activity.isEmpty()) {
            return Optional.empty();
        }

        List<Participant> participants = participantService
                .getAllParticipantsForActivityInstance(instance.get().getId());

        return Optional.of(activityMapper.toDashboardInstanceDto(activity.get(), instance.get(), participants.size()));
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

    public List<ActivityInstance> getActivityInstances(UUID activityId) {
        Optional<Activity> activity = activityService.getActivity(activityId);
        if (activity.isEmpty()) {
            logger.warn("no activity found with given ID, '{}'", activityId);
            return new ArrayList<>();
        }
        return activityInstanceService.getAllInstancesForActivity(activity.get());
    }
}
