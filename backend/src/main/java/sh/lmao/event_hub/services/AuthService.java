package sh.lmao.event_hub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sh.lmao.event_hub.entities.User;
import sh.lmao.event_hub.exceptions.AlreadyExistsException;
import sh.lmao.event_hub.models.LoginCreds;
import sh.lmao.event_hub.repositories.UserRepo;
import sh.lmao.event_hub.security.JWTUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(User user) throws AlreadyExistsException {
        if (userRepo.findByUsername(user.getUsername()).isPresent()
                || userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new AlreadyExistsException("user already exists");
        }

        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        user = userRepo.save(user);

        return jwtUtil.generateToken(user.getUsername());
    }

    public String loginUser(LoginCreds creds) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                creds.getUsername(), creds.getPassword());
        authenticationManager.authenticate(authInputToken);

        return jwtUtil.generateToken(creds.getUsername());
    }
}
