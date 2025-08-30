package sh.lmao.event_hub.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;
import sh.lmao.event_hub.entities.RefreshToken;
import sh.lmao.event_hub.entities.User;
import sh.lmao.event_hub.exceptions.NotFoundException;
import sh.lmao.event_hub.exceptions.TokenExpiredException;
import sh.lmao.event_hub.repositories.RefreshTokenRepo;
import sh.lmao.event_hub.repositories.UserRepo;

@Service
public class RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    final public String refreshTokenKey = "refresh-token";

    final public int expireInDays = 7;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Scheduled(fixedRate = 6, timeUnit = TimeUnit.HOURS)
    @Transactional
    public void cleanupExpiredTokens() {
        int count = refreshTokenRepo.deleteByExpiresAtBefore(LocalDateTime.now());
        if (count > 0) {
            logger.info("cleaned up {} expired refresh tokens", count);
        }
    }

    public RefreshToken createTokenFromUsername(String username) throws Exception {
        User user = userRepo.findByUsername(username).orElseThrow();
        return createToken(user.getId());
    }

    public RefreshToken createToken(UUID userId) {
        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(userRepo.getReferenceById(userId));
        token.setExpiresAt(LocalDateTime.now().plusDays(expireInDays));
        return refreshTokenRepo.save(token);
    }

    public RefreshToken saveToken(RefreshToken refreshToken) {
        return refreshTokenRepo.save(refreshToken);
    }

    @Transactional
    public void deleteToken(String token) {
        refreshTokenRepo.deleteByToken(token);
    }

    @Transactional
    public void deleteTokensForUser(UUID userId) {
        refreshTokenRepo.deleteByUserId(userId);
    }

    public Optional<RefreshToken> getToken(String token) {
        return refreshTokenRepo.findByToken(token);
    }

    public Cookie createCookie(String token) {
        Cookie refreshCookie = new Cookie(refreshTokenKey, token);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setSecure(true);
        refreshCookie.setMaxAge(token.length() != 0 ? (int) expireInDays * 24 * 60 * 60 : 0);
        return refreshCookie;
    }

    public Optional<String> extractTokenFromCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(refreshTokenKey)) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }
}
