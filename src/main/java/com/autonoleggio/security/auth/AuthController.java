package com.autonoleggio.security.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin("${allowed.origins}")
public class AuthController {

    private final AuthService authService;

    @Value("${allowed.origins}")
    private String allowedOrigins;

    @PostMapping("/register")
    ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok( authService.register(request) );
    }

    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok( authService.login(request) );
    }

}
