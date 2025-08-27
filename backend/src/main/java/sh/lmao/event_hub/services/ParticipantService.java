package sh.lmao.event_hub.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import sh.lmao.event_hub.dto.mappers.ParticipantMapper;
import sh.lmao.event_hub.dto.request.ParticipantDTO;
import sh.lmao.event_hub.entities.ActivityOption;
import sh.lmao.event_hub.entities.Participant;
import sh.lmao.event_hub.repositories.ActivityOptionRepo;
import sh.lmao.event_hub.repositories.ParticipantRepo;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepo participantRepo;

    @Autowired
    private ParticipantActivityOptionService participantActivityOptionService;

    @Autowired
    private ActivityOptionRepo activityOptionRepo;

    @Autowired
    private ParticipantMapper participantMapper;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    private void scheduleAnonymizeParticipants() {
        // for (Activity activity : activityRepo.findAll()) {
        //     initFuturePopulating(activity);
        // }
    }

    public List<ParticipantDTO> getAllParticipantsForActivityInstance(UUID activityInstanceId) {
        List<Participant> participants = participantRepo.findAllByActivityId(activityInstanceId);
        List<ParticipantDTO> dtos = new ArrayList<>();

        // TODO: this could be done elegantly with a collection stream & mapping
        for (Participant participant : participants) {
            List<UUID> ids = participantActivityOptionService.getActivityOptionIdsForParticipant(participant.getId());
            List<ActivityOption> options = ids.stream()
                    .map(id -> activityOptionRepo.findById(id).get())
                    .collect(Collectors.toList());
            dtos.add(participantMapper.fromParticipantToDTO(participant, options));
        }

        return dtos;
    }

    public int getParticipantCountForActivityInstance(UUID activityInstanceId) {
        return participantRepo.findAllByActivityId(activityInstanceId).size();
    }
}
