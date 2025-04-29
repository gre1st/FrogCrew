package edu.tcu.cs.frogcrew.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class SecurityConfiguration {

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    @Value("${api.endpoint.base-url}")
    private String baseUrl;

    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
    private final CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;
    private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;

    public SecurityConfiguration(CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint, CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint, CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler) throws NoSuchAlgorithmException {
        this.customBasicAuthenticationEntryPoint = customBasicAuthenticationEntryPoint;
        this.customBearerTokenAuthenticationEntryPoint = customBearerTokenAuthenticationEntryPoint;
        this.customBearerTokenAccessDeniedHandler = customBearerTokenAccessDeniedHandler;

        // Generate a public/private key pair.
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // generated key size = 2048 bits
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
    }

    /* URL Path Authorization
    /crewMember/**                      ROLE_CREW_MEMBER, ROLE_ADMIN
    /availability/**                    ROLE_CREW_MEMBER, ROLE_ADMIN
    /games/** (view)                    ROLE_CREW_MEMBER, ROLE_ADMIN
    /games/** (edit, create, publish)   ROLE_ADMIN
    /notifications/**                   ROLE_CREW_MEMBER, ROLE_ADMIN
    /positions/**                       ROLE_ADMIN,
    /templates/**                       ROLE_ADMIN,
    /reports/**                         ROLE_ADMIN
    */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // Crew Member Endpoints
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/crewMember/{userId}").hasAnyAuthority("ROLE_CREW", "ROLE_ADMIN") // Use Case 3
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/crewMember").hasAnyAuthority("ROLE_CREW", "ROLE_ADMIN") // Use Case 1
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/crewMember/{userId}").hasAnyAuthority("ROLE_CREW", "ROLE_ADMIN") // Use Case 2, 19
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/crewMember/{userId}/schedule/{scheduleId}").hasAnyAuthority("ROLE_CREW", "ROLE_ADMIN") // Use Case 4
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/crewSchedule/{gameId}").hasAnyAuthority("ROLE_CREW", "ROLE_ADMIN") // Use Case 5
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/crewList/{gameId}").hasAnyAuthority("ROLE_CREW", "ROLE_ADMIN") // Use Case 6
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/availability").hasAnyAuthority("ROLE_CREW", "ROLE_ADMIN") // Use Case 7
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/availability").hasAnyAuthority("ROLE_CREW", "ROLE_ADMIN") // Use Case 8
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/scheduledGames/pickup/{tradeId}/{userId}").hasAnyAuthority("ROLE_CREW", "ROLE_ADMIN") // Use Case 9
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/scheduledGames/approve/{tradeId}").hasAnyAuthority("ROLE_CREW", "ROLE_ADMIN") // Use Case 10
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/scheduledGames/deny/{tradeId}").hasAnyAuthority("ROLE_CREW", "ROLE_ADMIN") // Use Case 10
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/notifications/{userId}").hasAnyAuthority("ROLE_CREW", "ROLE_ADMIN") // Use Case 12
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/notifications/{notificationId}").hasAnyAuthority("ROLE_CREW", "ROLE_ADMIN") // Use Case 13
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/scheduledGames/get/{userId}").hasAnyAuthority("ROLE_CREW", "ROLE_ADMIN") // Use Case 4
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/auth/login").permitAll() // Use Case 14

                        // Admin Endpoints
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/Admin/UpdateGameTimes").hasAuthority("ROLE_ADMIN") // Use Case 18
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/Admin/RefreshTestData").hasAuthority("ROLE_ADMIN") // Use Case 18
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/AdminClearTestData").hasAuthority("ROLE_ADMIN") // Use Case 18
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/report/crewMember/{userId}/{season}").hasAuthority("ROLE_ADMIN") // Use Case 26
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/report/financial/{season}/{sport}").hasAuthority("ROLE_ADMIN") // Use Case 26
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/report/position/{positionId}/{season}").hasAuthority("ROLE_ADMIN") // Use Case 27
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/report/position/{positionId}/{season}/{sport}").hasAuthority("ROLE_ADMIN") // Use Case 27
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/positions").hasAuthority("ROLE_ADMIN") // Use Case 29
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/positions").hasAuthority("ROLE_ADMIN") // Use Case 29
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/positions/{positionId}").hasAuthority("ROLE_ADMIN") // Use Case 30
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/positions/properties/{gameType}").hasAuthority("ROLE_ADMIN") // Use Case 31
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/template").hasAuthority("ROLE_ADMIN") // Use Case 32
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/template/{templateId}").hasAuthority("ROLE_ADMIN") // Use Case 34
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/template").hasAuthority("ROLE_ADMIN") // Use Case 32
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/template/{templateId}").hasAuthority("ROLE_ADMIN") // Use Case 35
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/template/{templateId}").hasAuthority("ROLE_ADMIN") // Use Case 36
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/crewList/export/{gameId}").hasAuthority("ROLE_ADMIN") // Use Case 5
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/admin/invite").hasAuthority("ROLE_ADMIN") // Use Case 14
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/crewMember").hasAuthority("ROLE_ADMIN") // Use Case 16
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/crewMember/disable/{userId}").hasAuthority("ROLE_ADMIN") // Use Case 15
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/crewMember/{userId}/availability").hasAuthority("ROLE_ADMIN") // Use Case 17
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/gameSchedule/games").hasAuthority("ROLE_ADMIN") // Use Case 5
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/gameSchedule").hasAuthority("ROLE_ADMIN") // Use Case 18
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/gameSchedule").hasAuthority("ROLE_ADMIN") // Use Case 21
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/gameSchedule/season/{season}").hasAuthority("ROLE_ADMIN") // Use Case 5
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/gameSchedule/publish/{scheduleId}").hasAuthority("ROLE_ADMIN") // Use Case 24
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/gameSchedule/{scheduleId}/games").hasAuthority("ROLE_ADMIN") // Use Case 20
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/admin").hasAuthority("ROLE_ADMIN") // Admin dashboard
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(this.customBasicAuthenticationEntryPoint))
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(this.customBearerTokenAuthenticationEntryPoint)
                        .accessDeniedHandler(this.customBearerTokenAccessDeniedHandler))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        JWKSource<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;

    }

}
