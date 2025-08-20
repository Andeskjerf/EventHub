package sh.lmao.event_hub.controllers;

import java.util.List;
import java.util.Map;
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
import sh.lmao.event_hub.services.ActivityService;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody Activity activity) {
        try {
            activity = activityService.createActivity(activity);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("activity", activity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e));
        }
    }

    @PostMapping("/{activityId}/participants")
    public ResponseEntity<Map<String, Object>> addParticipant(
            @PathVariable UUID activityId,
            @Valid @RequestBody Participant participant) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("participant", activityService.addParticipantForActivity(activityId, participant)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e));
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
