package sh.lmao.event_hub.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sh.lmao.event_hub.entities.Participant;
import sh.lmao.event_hub.repositories.ParticipantRepo;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepo participantRepo;

    public List<Participant> getAllParticipantsForActivityInstance(UUID activityInstanceId) {
        return participantRepo.findByActivityId(activityInstanceId);
    }
}
