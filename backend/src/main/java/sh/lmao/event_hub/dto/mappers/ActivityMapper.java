package sh.lmao.event_hub.dto.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import sh.lmao.event_hub.dto.request.ActivityDTO;
import sh.lmao.event_hub.dto.request.CreateActivityDTO;
import sh.lmao.event_hub.dto.response.ActivityInstanceDTO;
import sh.lmao.event_hub.entities.Activity;
import sh.lmao.event_hub.entities.ActivityInstance;
import sh.lmao.event_hub.entities.ActivityOption;

@Component
public class ActivityMapper {

    public ActivityInstanceDTO toDashboardInstanceDto(Activity activity, ActivityInstance instance,
            List<ActivityOption> options,
            int participantCount) {
        ActivityInstanceDTO dto = new ActivityInstanceDTO();
        dto.setActivityId(activity.getId());
        dto.setInstanceId(instance.getId());
        dto.setName(activity.getName());
        dto.setEventDate(instance.getEventDate());
        dto.setRegisterBefore(activity.getRegisterBefore());
        dto.setLocation(activity.getLocation());
        dto.setMeetLocation(activity.getMeetLocation());
        dto.setDescription(activity.getDescription());
        dto.setMaxParticipants(activity.getMaxParticipants());
        dto.setRepeatInterval(activity.getRepeatInterval());
        dto.setOptions(options);
        dto.setParticipants(participantCount);
        return dto;
    }

    public Activity fromCreateActivityDtoToActivity(CreateActivityDTO createActivityDTO) {
        Activity activity = new Activity();
        activity.setName(createActivityDTO.getName());
        activity.setEventDate(createActivityDTO.getEventDate());
        activity.setRegisterBefore(createActivityDTO.getRegisterBefore());
        activity.setLocation(createActivityDTO.getLocation());
        activity.setMeetLocation(createActivityDTO.getMeetLocation());
        activity.setDescription(createActivityDTO.getDescription());
        activity.setMaxParticipants(createActivityDTO.getMaxParticipants());
        activity.setRepeatInterval(createActivityDTO.getRepeatInterval());
        return activity;
    }
}
