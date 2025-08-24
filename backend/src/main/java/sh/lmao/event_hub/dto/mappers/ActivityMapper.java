package sh.lmao.event_hub.dto.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import sh.lmao.event_hub.dto.response.ActivityInstanceDTO;
import sh.lmao.event_hub.entities.Activity;
import sh.lmao.event_hub.entities.ActivityInstance;

@Component
public class ActivityMapper {

    public ActivityInstanceDTO toDashboardInstanceDto(Activity activity, ActivityInstance instance) {
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
        return dto;
    }
}
