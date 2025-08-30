package sh.lmao.event_hub.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Component
public class JWTUtil {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    @Value("${jwt.public.key}")
    RSAPublicKey key;

    @Value("${jwt.private.key}")
    RSAPrivateKey priv;

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
        return jwtEncoder().encode(JwtEncoderParameters.from(claims)).getTokenValue();

    }

    public boolean isJwtTokenValid(String token) {
        // we check the validity by the token by simply trying to decode it
        // it'll fail with a signature error if it's invalid
        try {
            jwtDecoder().decode(token);
            return true;
        } catch (BadJwtException e) {
            logger.warn("failed to verify JWT token signature. this is most likely due to new RSA keys.");
            return false;
        } catch (Exception e) {
            logger.error("got an unknown error while checking JWT token validity!");
            logger.error(e.toString());
            return false;
        }
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
