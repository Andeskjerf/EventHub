package sh.lmao.event_hub.security;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import sh.lmao.event_hub.entities.User;
import sh.lmao.event_hub.repositories.UserRepo;

@Component
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("TRYING TO LOAD USER BY USERNAME AHHAAHHAHA");
        Optional<User> userRes = userRepo.findByUsername(username);

        if (userRes.isEmpty()) {
            throw new UsernameNotFoundException("No user found with this username");
        }

        User user = userRes.get();
        return new org.springframework.security.core.userdetails.User(
                username,
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
