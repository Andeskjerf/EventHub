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

import sh.lmao.event_hub.dto.request.ActivityDTO;
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

    public Activity createActivity(Activity activity) throws AlreadyExistsException {
        Optional<Activity> existing = activityRepo.findByNameAndActive(activity.getName(), true);
        // FIXME: ... maybe not ideal?
        // we want to be able to create new activities with the same names
        // as long as the other activities are not active, e.g deleted
        // maybe not smart?
        if (existing.isPresent()) {
            throw new AlreadyExistsException("activity exists");
        }

        Activity savedActivity = activityRepo.save(activity);
        return activity;
    }

    public Optional<Activity> getActivity(UUID id) {
        return activityRepo.findById(id);
    }

    public List<Activity> getAllActivities() {
        return activityRepo.findAll();
    }

}
