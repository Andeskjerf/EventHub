package sh.lmao.event_hub.security;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    @Autowired
    JwtEncoder encoder;

    @Autowired
    JwtDecoder decoder;

    public static long tokenExpiration = 36000L;

    public String generateToken(String username) {
        Instant now = Instant.now();
        // String scope = authentication.getAuthorities().stream()
        // .map(GrantedAuthority::getAuthority)
        // .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(tokenExpiration))
                .subject(username)
                .claim("scope", "app")
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        // String scope = authentication.getAuthorities().stream()
        // .map(GrantedAuthority::getAuthority)
        // .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(tokenExpiration))
                .subject(authentication.getName())
                .claim("scope", "app")
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    }

    public void isJwtTokenValid(String token) {
        Jwt jwt = decoder.decode(token);

    }
}
