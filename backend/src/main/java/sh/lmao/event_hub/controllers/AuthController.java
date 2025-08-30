package sh.lmao.event_hub.controllers;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import sh.lmao.event_hub.entities.RefreshToken;
import sh.lmao.event_hub.entities.User;
import sh.lmao.event_hub.models.LoginCreds;
import sh.lmao.event_hub.security.JWTUtil;
import sh.lmao.event_hub.services.AuthService;
import sh.lmao.event_hub.services.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${REGISTRATION_ENABLED:false}")
    private boolean registrationEnabled;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerHandler(
            @Valid @RequestBody User user, HttpServletResponse response) {
        if (!registrationEnabled) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "registration is currently disabled"));
        }

        try {
            // FIXME: should the controller orchestrate this?
            // seems like a lot of boilerplate if all we need is to get token & refresh
            // token
            // easier to just do it here
            user = authService.registerUser(user);
            String jwtToken = jwtUtil.generateToken(user.getUsername());
            RefreshToken refreshToken = refreshTokenService.createToken(user.getId());
            response.addCookie(authService.createCookie(jwtToken));
            response.addCookie(refreshTokenService.createCookie(refreshToken.getToken()));

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "registration successful", "username", user.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "user exists"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginHandler(
            @RequestBody LoginCreds body, HttpServletResponse response) {
        try {
            // ...should we be returning something?
            // it'll throw an exception if it fails so maybe not?
            authService.loginUser(body);
            String token = jwtUtil.generateToken(body.getUsername());
            RefreshToken refreshToken = refreshTokenService.createTokenFromUsername(body.getUsername());
            response.addCookie(refreshTokenService.createCookie(refreshToken.getToken()));
            response.addCookie(authService.createCookie(token));

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "login successful", "username", body.getUsername()));
        } catch (AuthenticationException authExc) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "incorrect username / password"));
        } catch (Exception e) {
            logger.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "login failed for unknown reason"));
        }
    }

    @GetMapping("/logout")
    public Map<String, Object> logoutHandler(
            HttpServletRequest request,
            HttpServletResponse response) {
        authService.logout(refreshTokenService.extractTokenFromCookies(request.getCookies()));
        response.addCookie(refreshTokenService.createCookie(""));
        response.addCookie(authService.createCookie(""));
        return Map.of("message", "logout successful");
    }
}
