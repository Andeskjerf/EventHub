package sh.lmao.event_hub.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import sh.lmao.event_hub.entities.User;
import sh.lmao.event_hub.models.LoginCreds;
import sh.lmao.event_hub.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerHandler(
            @Valid @RequestBody User user, HttpServletResponse response) {
        try {
            String token = authService.registerUser(user);
            response.addCookie(authService.createCookie(token));

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
            String token = authService.loginUser(body);
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
            HttpServletResponse response) {
        response.addCookie(authService.logout());
        return Map.of("message", "logout successful");
    }
}
