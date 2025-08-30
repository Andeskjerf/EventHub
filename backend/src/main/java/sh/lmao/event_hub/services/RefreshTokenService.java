package sh.lmao.event_hub.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;
import sh.lmao.event_hub.entities.RefreshToken;
import sh.lmao.event_hub.repositories.RefreshTokenRepo;
import sh.lmao.event_hub.repositories.UserRepo;

@Service
public class RefreshTokenService {

    final public String refreshTokenKey = "refresh-token";

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    public RefreshToken createToken(UUID userId) {
        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(userRepo.getReferenceById(userId));
        return refreshTokenRepo.save(token);
    }

    @Transactional
    public void revokeToken(String token) {
        refreshTokenRepo.deleteByToken(token);
    }

    @Transactional
    public void revokeTokensForUser(UUID userId) {
        refreshTokenRepo.deleteByUserId(userId);
    }

    public Optional<String> extractTokenFromCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName() == refreshTokenKey) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }
}
