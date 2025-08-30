package sh.lmao.event_hub.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sh.lmao.event_hub.entities.RefreshToken;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    void deleteByUserId(UUID userId);
}
