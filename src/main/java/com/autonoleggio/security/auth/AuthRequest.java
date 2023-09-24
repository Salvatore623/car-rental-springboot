package com.autonoleggio.security.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(

        @Size(min = 1, max = 50)
        @NotBlank(message = "Il campo non può essere vuoto")
        String fullName,
        @NotBlank(message = "Il campo non può essere vuoto")
        @Email(message = "Inserisci un'email valida")
        @Size(min = 1, max = 50)
        String email,
        @NotBlank(message = "Il campo non può essere vuoto")
        @Size(min = 8, max = 255, message = "Password minimo 8 caratteri")
        String password) {
}
