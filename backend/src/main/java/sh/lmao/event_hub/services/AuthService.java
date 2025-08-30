package sh.lmao.event_hub.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import sh.lmao.event_hub.entities.RefreshToken;
import sh.lmao.event_hub.entities.User;
import sh.lmao.event_hub.exceptions.AlreadyExistsException;
import sh.lmao.event_hub.models.LoginCreds;
import sh.lmao.event_hub.repositories.RefreshTokenRepo;
import sh.lmao.event_hub.repositories.UserRepo;
import sh.lmao.event_hub.security.JWTUtil;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) throws AlreadyExistsException {
        if (userRepo.findByUsername(user.getUsername()).isPresent()
                || userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new AlreadyExistsException("user already exists");
        }

        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        return userRepo.save(user);
    }

    public void loginUser(LoginCreds creds) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                creds.getUsername(), creds.getPassword());
        authenticationManager.authenticate(authInputToken);
    }

    public Cookie logout(Optional<String> refreshToken) throws AuthenticationException {
        // we log the user out by giving them an expired jwt-token cookie
        // in the future, we might want session & refresh tokens
        // when we get there, this should also invalidate the refresh token
        if (refreshToken.isPresent()) {
            refreshTokenService.revokeToken(refreshToken.get());
        } else {
            logger.warn("no refresh token was provided, unable to revoke. returning empty jwt token anyway");
        }
        return createCookie("");
    }

    public Cookie createCookie(String token) {
        Cookie jwtCookie = new Cookie("jwt-token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setSecure(true);
        jwtCookie.setMaxAge(token.length() != 0 ? (int) JWTUtil.tokenExpiration : 0);
        return jwtCookie;
    }
}
