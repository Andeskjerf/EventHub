package sh.lmao.event_hub.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sh.lmao.event_hub.entities.Activity;
import sh.lmao.event_hub.entities.Participant;
import sh.lmao.event_hub.repositories.ActivityRepo;
import sh.lmao.event_hub.repositories.ParticipantRepo;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    private ActivityRepo activityRepo;

    @Autowired
    private ParticipantRepo participantRepo;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody Activity activity) {
        if (activityRepo.findByName(activity.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "activity exists"));
        }

        activity = activityRepo.save(activity);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("activity", activity));
    }

    @PostMapping("/{activityId}/participants")
    public ResponseEntity<Map<String, Object>> addParticipant(
            @PathVariable UUID activityId,
            @Valid @RequestBody Participant participant) {
        Optional<Activity> activity = activityRepo.findById(activityId);
        if (activity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        participant.setActivity(activity.get());
        participant = participantRepo.save(participant);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("participant", participant));
    }

    @GetMapping("/{activityId}/participants")
    public List<Participant> getParticipants(
            @PathVariable UUID activityId) {
        return participantRepo.findByActivityId(activityId);
    }

    @GetMapping("/all")
    public List<Activity> getAll() {
        return activityRepo.findAll();
    }
}
