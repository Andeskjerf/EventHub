package sh.lmao.event_hub.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sh.lmao.event_hub.entities.Participant;

@Repository
public interface ParticipantRepo extends JpaRepository<Participant, UUID> {
    List<Participant> findByActivityId(UUID activityId);
}
