package sh.lmao.event_hub.dto.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import sh.lmao.event_hub.dto.request.ActivityDTO;
import sh.lmao.event_hub.dto.request.CreateActivityDTO;
import sh.lmao.event_hub.dto.request.RegisterParticipantDTO;
import sh.lmao.event_hub.dto.response.ActivityInstanceDTO;
import sh.lmao.event_hub.entities.Activity;
import sh.lmao.event_hub.entities.ActivityInstance;
import sh.lmao.event_hub.entities.ActivityOption;
import sh.lmao.event_hub.entities.Participant;

@Component
public class ParticipantMapper {

    public Participant fromRegisterParticipantDtoToParticipant(RegisterParticipantDTO dto, ActivityInstance instance) {
        Participant participant = new Participant();
        participant.setActivityInstance(instance);
        participant.setName(dto.getName());
        participant.setPhoneNumber(dto.getPhoneNumber());
        return participant;
    }
}
