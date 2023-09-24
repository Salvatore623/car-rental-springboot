package com.autonoleggio.security.user;

import com.autonoleggio.security.auth.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@CrossOrigin("${allowed.origins}")
public class UserController {

    private final UserService userService;

    @Value("${allowed.origins}")
    private String allowedOrigins;

    @PatchMapping("/fullName")
    ResponseEntity<AuthResponse> changeFullName(@RequestBody String fullName){
        return ResponseEntity.ok( userService.changeFullName(fullName) );
    }

    @PatchMapping("/email")
    ResponseEntity<AuthResponse> changeEmail(@RequestBody String email){
        return ResponseEntity.ok( userService.changeEmail(email) );
    }

    @PatchMapping("/password")
    ResponseEntity<AuthResponse> changePassword(@RequestBody changePasswordReq passwords){
        return ResponseEntity.ok( userService.changePassword(passwords) );
    }

}
