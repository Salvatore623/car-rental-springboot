package com.autonoleggio.security.admin;

import com.autonoleggio.car.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@CrossOrigin("${allowed.origins}")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @Value("${allowed.origins}")
    private String allowedOrigins;

    @PostMapping("/car")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<Car> newCar(@RequestBody Car car) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.newCar(car));
    }

    @PatchMapping("/change-role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<Map<String, String>> changeRole(@RequestBody String email){
        adminService.changeRole(email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Ruolo cambiato con successo");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public boolean verifyAdmin(){
        return true;
    }

}
