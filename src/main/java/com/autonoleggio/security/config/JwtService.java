package com.autonoleggio.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public String generateToken(UserDetails userDetails, String fullName) {
        Consumer<Map<String, Object>> claims = map -> {
            var roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            map.put("name", fullName);
            map.put("role", roles);
        };
        return generateToken(claims, userDetails);
    }


    public String generateToken(Consumer<Map<String, Object>> extraClaims, UserDetails userDetails) {
        var claims = JwtClaimsSet.builder()
                .subject(userDetails.getUsername())
                .claims(extraClaims)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
