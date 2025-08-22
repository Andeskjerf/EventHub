package sh.lmao.event_hub.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
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
    public Map<String, Object> loginHandler(
            @RequestBody LoginCreds body, HttpServletResponse response) {
        try {
            String token = authService.loginUser(body);
            response.addCookie(authService.createCookie(token));

            return Map.of("message", "login successful", "username", body.getUsername());
        } catch (AuthenticationException authExc) {
            throw new RuntimeException("invalid username/password");
        }

    }
}
