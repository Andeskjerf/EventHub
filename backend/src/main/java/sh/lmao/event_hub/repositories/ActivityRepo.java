package sh.lmao.event_hub.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sh.lmao.event_hub.entities.Activity;

@Repository
public interface ActivityRepo extends JpaRepository<Activity, UUID> {
    Optional<Activity> findByName(String name);

    List<Activity> findAllByActive(boolean active);

    Optional<Activity> findByNameAndActive(String name, boolean active);
}
