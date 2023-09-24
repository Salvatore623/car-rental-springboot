package com.autonoleggio.security.admin;


import com.autonoleggio.car.Car;
import com.autonoleggio.car.CarRepository;
import com.autonoleggio.security.user.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.autonoleggio.security.user.Role.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CarRepository carRepository;

    public Car newCar(Car car) {
        return carRepository.save(car);
    }

    public void changeRole(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Username non trovata"));

        if(user.getRole().name().equals(ROLE_ADMIN.name())){
            throw new EntityExistsException("Il ruolo di " + email + " è già admin");
        }

        user.setRole(ROLE_ADMIN);
        userRepository.save(user);
    }
}
