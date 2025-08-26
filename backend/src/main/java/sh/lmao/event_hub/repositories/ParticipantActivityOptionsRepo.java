package sh.lmao.event_hub.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sh.lmao.event_hub.entities.ParticipantActivityOption;

@Repository
public interface ParticipantActivityOptionsRepo extends JpaRepository<ParticipantActivityOption, UUID> {
    public List<ParticipantActivityOption> findAllByParticipantId(UUID participantId);
}
