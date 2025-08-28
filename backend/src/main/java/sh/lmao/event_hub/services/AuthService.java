package sh.lmao.event_hub.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private JWTUtil jwtUtil;

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
        user = userRepo.save(user);

        return jwtUtil.generateToken(user.getUsername());
    }

    public User loginUser(LoginCreds creds) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                creds.getUsername(), creds.getPassword());
        authenticationManager.authenticate(authInputToken);

        return jwtUtil.generateToken(creds.getUsername());
    }

    public Cookie logout() throws AuthenticationException {
        // we log the user out by giving them an expired jwt-token cookie
        // in the future, we might want session & refresh tokens
        // when we get there, this should also invalidate the refresh token
        return createCookie("");
    }

    public Cookie createCookie(String token) {
        Cookie jwtCookie = new Cookie("jwt-token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setSecure(false); // FIXME: set to true when in production!
        jwtCookie.setMaxAge(token.length() != 0 ? (int) JWTUtil.tokenExpiration : 0);
        return jwtCookie;
    }

    private RefreshToken createRefreshToken(UUID userId) {
        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(userRepo.getReferenceById(userId));
        token.setExpiresAt(LocalDateTime.now().plusDays(7));
        return refreshTokenRepo.save(token);
    }
}
