package com.autonoleggio.mailsender;

import com.autonoleggio.booking.Booking;
import com.autonoleggio.booking.BookingRepository;
import com.autonoleggio.exception.BookingStatusNotChangeable;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.autonoleggio.booking.BookingStatus.ATTIVA;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final BookingRepository bookingRepository;

    @Value("spring.mail.username")
    private String fromMail;

    public void sendMailBookingConfirm(String mail, String id) {
        var simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject("Conferma prenotazione");
        String confirmationLink = "https://quiz-server-rjub.onrender.com/api/v1/auth/booking/confirm/" + id;
        String htmlContent = "Clicca il link per confermare la tua prenotazione " + confirmationLink;
        simpleMailMessage.setText(htmlContent);
        simpleMailMessage.setTo(mail);

        javaMailSender.send(simpleMailMessage);
    }


    public ResponseEntity<String> confirmBooking(String id) throws IOException {
        var booking = bookingRepository.findById(Integer.parseInt(id));
        if (booking.isEmpty()) {
            return getHtmlResponse("/static/booking-not-found.html", HttpStatus.BAD_REQUEST);
        }
        return bookingStatus(booking.get());
    }


    private ResponseEntity<String> bookingStatus(Booking booking) throws IOException {
        switch (booking.getStatus()) {
            case NON_ATTIVA -> {
                booking.setStatus(ATTIVA);
                bookingRepository.save(booking);
                return getHtmlResponse("/static/booking-confirmed.html", HttpStatus.OK);
            }
            case SCADUTA -> {
                return getHtmlResponse("/static/booking-expired.html", HttpStatus.BAD_REQUEST);
            }
            case CANCELLATA -> {
                return getHtmlResponse("/static/booking-deleted.html", HttpStatus.NOT_FOUND);
            }
            case ATTIVA -> {
                return getHtmlResponse("/static/booking-already-confirmed.html", HttpStatus.OK);
            }
            default -> throw new BookingStatusNotChangeable("Non è stato possibile cambiare lo stato della prenotazione");
        }
    }

    private ResponseEntity<String> getHtmlResponse(String htmlFilePath, HttpStatus status) throws IOException {
        Resource resource = new ClassPathResource(htmlFilePath);
        String htmlContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return ResponseEntity.status(status).contentType(MediaType.TEXT_HTML).body(htmlContent);
    }


}