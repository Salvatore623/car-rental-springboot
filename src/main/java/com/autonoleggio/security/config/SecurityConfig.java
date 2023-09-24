package com.autonoleggio.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import static com.autonoleggio.security.user.Role.ROLE_ADMIN;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtDecoder jwtDecoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       http
               .csrf(AbstractHttpConfigurer::disable)
               .cors(Customizer.withDefaults())
               .authorizeHttpRequests(auth -> {
                   auth.requestMatchers("/api/v1/auth/**").permitAll();
                   auth.requestMatchers("/api/v1/admin/**").hasAuthority(ROLE_ADMIN.name());
                   auth.anyRequest().authenticated();
               })
               .authenticationProvider(authenticationProvider)
               .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                   jwt.decoder(jwtDecoder);
                   jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
               }))
               .exceptionHandling(exceptions -> exceptions
                       .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                       .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
               );
       return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
