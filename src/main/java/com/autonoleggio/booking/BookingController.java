package com.autonoleggio.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("api/v1/booking")
@RequiredArgsConstructor
@CrossOrigin("${allowed.origins}")
public class BookingController {

    @Value("${allowed.origins}")
    private String allowedOrigins;

    private final BookingService bookingService;

    @PostMapping
    public void createBooking(@RequestBody @Valid Booking booking){
        bookingService.createBooking(booking);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Integer id){
        bookingService.deleteBooking(id);
    }

    @GetMapping("/user")
    ResponseEntity<List<BookingDto>> getBookingsByEmail() throws ParseException {
        return ResponseEntity.ok( bookingService.getBookingsByEmail() );
    }

}
