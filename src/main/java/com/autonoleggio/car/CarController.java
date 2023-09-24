package com.autonoleggio.car;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth/car")
@CrossOrigin("${allowed.origins}")
public class CarController {

    @Value("${allowed.origins}")
    private String allowedOrigins;

    private final CarService carService;

    @GetMapping("/all")
    ResponseEntity<List<Car>> getAllCars(){
        return ResponseEntity.ok( carService.getAllCars() );
    }

}
