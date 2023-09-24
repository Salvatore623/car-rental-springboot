package com.autonoleggio.security.auth;

import com.autonoleggio.security.config.JwtService;
import com.autonoleggio.security.user.Role;
import com.autonoleggio.security.user.User;
import com.autonoleggio.security.user.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register( AuthRequest req){
        userRepository.findByEmail(req.email())
                .ifPresent(user -> {
                    throw new EntityExistsException(user.getUsername() + " è già in uso");
                });
        var user = User.builder()
                .email(req.email())
                .fullName(req.fullName())
                .password(passwordEncoder.encode(req.password()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user, user.getFullName());

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                ));
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Email non trovata"));

        String token = jwtService.generateToken(user, user.getFullName());

        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
