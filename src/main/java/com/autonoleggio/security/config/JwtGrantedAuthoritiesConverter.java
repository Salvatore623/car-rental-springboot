package com.autonoleggio.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Configuration
public class JwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        String role = jwt.getClaim("role");
        if (role != null && !role.isEmpty()) {
            return Collections.singleton(new SimpleGrantedAuthority(role));
        }
        return Collections.emptyList();
    }


}
