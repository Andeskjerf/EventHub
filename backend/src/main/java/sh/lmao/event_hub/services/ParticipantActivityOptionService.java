package sh.lmao.event_hub.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sh.lmao.event_hub.entities.ParticipantActivityOption;
import sh.lmao.event_hub.repositories.ActivityOptionRepo;
import sh.lmao.event_hub.repositories.ParticipantActivityOptionsRepo;
import sh.lmao.event_hub.repositories.ParticipantRepo;

@Service
public class ParticipantActivityOptionService {

    @Autowired
    private ParticipantActivityOptionsRepo participantActivityOptionRepo;

    @Autowired
    private ParticipantRepo participantRepo;

    @Autowired
    private ActivityOptionRepo activityOptionRepo;

    public ParticipantActivityOption createEntity(UUID participantId, UUID optionId) {
        ParticipantActivityOption option = new ParticipantActivityOption();
        option.setParticipant(participantRepo.findById(participantId).get());
        option.setActivityOption(activityOptionRepo.findById(optionId).get());
        option.setParticipantId(participantId);
        option.setActivityOptionId(optionId);
        return participantActivityOptionRepo.save(option);
    }

    public List<UUID> getActivityOptionIdsForParticipant(UUID participantId) {
        List<ParticipantActivityOption> relations = participantActivityOptionRepo.findAllByParticipantId(participantId);
        return relations.stream()
                .map(relation -> relation.getActivityOptionId())
                .collect(Collectors.toList());
    }
}
