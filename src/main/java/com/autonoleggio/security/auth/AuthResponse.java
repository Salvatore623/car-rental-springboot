package com.autonoleggio.security.auth;

import lombok.Builder;

@Builder
public record AuthResponse(String token) {
}
