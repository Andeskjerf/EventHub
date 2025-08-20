package sh.lmao.event_hub.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sh.lmao.event_hub.entities.Activity;
import sh.lmao.event_hub.entities.Participant;
import sh.lmao.event_hub.exceptions.AlreadyExistsException;
import sh.lmao.event_hub.exceptions.NotFoundException;
import sh.lmao.event_hub.repositories.ActivityRepo;
import sh.lmao.event_hub.repositories.ParticipantRepo;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepo activityRepo;

    @Autowired
    private ParticipantRepo participantRepo;

    public Activity createActivity(Activity activity) throws AlreadyExistsException {
        if (activityRepo.findByName(activity.getName()).isPresent()) {
            throw new AlreadyExistsException("activity exists");
        }

        return activityRepo.save(activity);
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
