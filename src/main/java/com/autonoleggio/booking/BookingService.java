package com.autonoleggio.booking;


import com.autonoleggio.car.CarRepository;
import com.autonoleggio.mailsender.MailService;
import com.autonoleggio.security.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.autonoleggio.booking.BookingStatus.*;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    public void createBooking(Booking booking){
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Email non trovata"));
        if(!booking.getTotalPrice().equals(totalPrice(booking.getCarName(), booking.getPickUpDate(), booking.getDropOfDate()))){
            throw new IllegalArgumentException("Il prezzo non è corretto");
        }
        booking.setUser(user);
        booking.setStatus(NON_ATTIVA);
        booking.setBookingDate(new Date(System.currentTimeMillis()));
        var b = bookingRepository.save(booking);

        mailService.sendMailBookingConfirm(user.getEmail(), b.getId().toString());

    }

    private Long totalPrice(String carName, String pickUpDate, String dropOfDate){
        var car = carRepository.findByName(carName)
                .orElseThrow(() -> new EntityNotFoundException("L'auto non esiste"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate pickUp = LocalDate.parse(pickUpDate, formatter);
        LocalDate dropOf = LocalDate.parse(dropOfDate, formatter);
        long days = Math.abs(pickUp.until(dropOf).getDays());
        return days * car.getPricePerDay();
    }

    public List<BookingDto> getBookingsByEmail() throws ParseException {
        List<BookingDto> bookingDto = new ArrayList<>();
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Email non trovata"));

        var list = bookingRepository.findAllByUserId(user.getId());
        for (var booking : list) {
                checkBookingIsStillActive(booking.getDropOfDate(), booking.getDropOfHour(), booking);
                    var b = BookingDto.builder()
                            .id(booking.getId())
                            .carName(booking.getCarName())
                            .bookingDate(booking.getBookingDate())
                            .status(booking.getStatus())
                            .pickUpLocation(booking.getPickUpLocation())
                            .dropOfLocation(booking.getDropOfLocation())
                            .pickUpDate(booking.getPickUpDate())
                            .dropOfDate(booking.getDropOfDate())
                            .pickUpHour(booking.getPickUpHour())
                            .dropOfHour(booking.getDropOfHour())
                            .totalPrice(booking.getTotalPrice())
                            .build();
                    bookingDto.add(b);
        }
        return bookingDto;
    }


    private void checkBookingIsStillActive(String day, String hour, Booking booking) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date bookingDate = dateFormat.parse(day + " " + hour);
        Date now = new Date();
        if (bookingDate.before(now)) {
            booking.setStatus(SCADUTA);
        }
    }

    public void deleteBooking(Integer id) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Email non trovata"));
        if(!user.getEmail().equals(email)){
            throw new AccessDeniedException("Accessio vietato");
        }
        var booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovata"));
        booking.setStatus(CANCELLATA);
        bookingRepository.save(booking);
    }



}
