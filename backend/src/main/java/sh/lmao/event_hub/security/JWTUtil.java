package sh.lmao.event_hub.security;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    JWTProvider jwtProvider;

    public static long tokenExpiration = 300L;

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
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    }

    public boolean isJwtTokenValid(String token) {
        // we check the validity by the token by simply trying to decode it
        // it'll fail with a signature error if it's invalid
        logger.info("validating token with public key hash: {}", jwtProvider.key.hashCode());
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (BadJwtException e) {
            logger.warn("failed to verify JWT token signature. this is most likely due to new RSA keys.");
            logger.warn(e.toString());
            return false;
        } catch (Exception e) {
            logger.error("got an unknown error while checking JWT token validity!");
            logger.error(e.toString());
            return false;
        }
    }
}
