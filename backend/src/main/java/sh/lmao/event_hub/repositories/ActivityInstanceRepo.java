package sh.lmao.event_hub.repositories;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sh.lmao.event_hub.entities.Activity;
import sh.lmao.event_hub.entities.ActivityInstance;

@Repository
public interface ActivityInstanceRepo extends JpaRepository<ActivityInstance, UUID> {
    Optional<ActivityInstance> findByEventDate(ZonedDateTime eventDate);

    List<ActivityInstance> findByActivityOrderByEventDate(Activity activity);

    Optional<ActivityInstance> findFirstByActivityAndEventDateAfterOrderByEventDate(Activity activity,
            ZonedDateTime currentDate);

    @Transactional
    void deleteByEventDateAfter(ZonedDateTime currentDate);
}
