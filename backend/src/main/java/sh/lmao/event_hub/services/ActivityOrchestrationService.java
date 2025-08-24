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
public class ActivityOrchestrationService {

    @Autowired
    private ActivityRepo activityRepo;

    @Autowired
    private ActivityInstanceRepo activityInstanceRepo;

    @Autowired
    private ParticipantRepo participantRepo;

    @Autowired
    private ActivityMapper activityMapper;


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
