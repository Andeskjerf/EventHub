package sh.lmao.event_hub.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;

import jakarta.validation.Valid;
import sh.lmao.event_hub.entities.Activity;
import sh.lmao.event_hub.entities.Participant;
import sh.lmao.event_hub.exceptions.AlreadyExistsException;
import sh.lmao.event_hub.services.ActivityService;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

    @Autowired
    private ActivityService activityService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody Activity activity) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("activity", activityService.createActivity(activity)));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "activity already exists"));
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "unexpected error"));
        }
    }

    @PostMapping("/{activityId}/participants")
    public ResponseEntity<Map<String, Object>> addParticipant(
            @PathVariable UUID activityInstanceId,
            @Valid @RequestBody Participant participant) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("participant",
                            activityService.addParticipantForActivity(activityInstanceId, participant)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "could not find activity to add participants to"));
        }
    }

    @GetMapping("/{activityId}/participants")
    public List<Participant> getParticipants(
            @PathVariable UUID activityId) {
        return activityService.getAllParticipantsForActivity(activityId);
    }

    @GetMapping("/all")
    public List<Activity> getAll() {
        return activityService.getAllActivities();
    }
}
