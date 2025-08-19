package sh.lmao.event_hub.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sh.lmao.event_hub.entities.Activity;
import sh.lmao.event_hub.repositories.ActivityRepo;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    private ActivityRepo activityRepo;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody Activity activity) {
        if (activityRepo.findByName(activity.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "activity exists"));
        }

        activity = activityRepo.save(activity);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("activity", activity));
    }

    @GetMapping("/all")
    public List<Activity> getAll() {
        return activityRepo.findAll();
    }
}
