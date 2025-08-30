package sh.lmao.event_hub.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import sh.lmao.event_hub.filters.JwtCookieAuthenticationFilter;
import sh.lmao.event_hub.security.MyUserDetailService;

@Configuration
public class SecurityConfig {

    @Value("${DOMAIN:http://localhost:*}")
    private String domain;

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private JwtCookieAuthenticationFilter jwtCookieAuthenticationFilter;

    private static final String[] PUBLIC_ALL_METHODS = {
            "/api/auth/**",
    };

    private static final String[] PUBLIC_GET_ONLY = {
            "/api/activity/all",
            "/api/activity/next-active",
            "/api/activity/*",
            "/api/activity/*/instances",
    };

    private static final String[] PUBLIC_POST_ONLY = {
            "/api/activity/*/participants",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(PUBLIC_ALL_METHODS).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_ONLY).permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_POST_ONLY).permitAll()
                        .anyRequest().authenticated()) // .csrf((csrf) -> csrf.ignoringRequestMatchers("/token"))
                // .httpBasic(Customizer.withDefaults())
                .csrf((csrf) -> csrf.disable())
                .httpBasic((httpBasic) -> httpBasic.disable())
                .oauth2ResourceServer((jwt) -> jwt.jwt(Customizer.withDefaults()))
                .userDetailsService(userDetailService)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtCookieAuthenticationFilter, BearerTokenAuthenticationFilter.class)
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));
        // http.addFilterBefore(filter, beforeFilter);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(domain));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }

}
