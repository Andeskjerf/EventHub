package sh.lmao.event_hub.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sh.lmao.event_hub.entities.Activity;
import sh.lmao.event_hub.entities.ActivityOption;
import sh.lmao.event_hub.repositories.ActivityOptionRepo;

@Service
public class ActivityOptionService {

    @Autowired
    ActivityOptionRepo activityOptionRepo;

    public void createOptionsFromList(List<String> options, Activity activity) {
        for (String option : options) {
            ActivityOption activityOption = new ActivityOption();
            activityOption.setName(option);
            activityOption.setActivity(activity);
            activityOptionRepo.save(activityOption);
        }
    }
}
