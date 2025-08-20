package sh.lmao.event_hub.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sh.lmao.event_hub.entities.ActivityOption;

@Repository
public interface ActivityOptionRepo extends JpaRepository<ActivityOption, UUID> {
}
