package com.autonoleggio.mailsender;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class MailController {

    private final MailService mailService;

    @GetMapping("/booking/confirm/{id}")
    public ResponseEntity<String> sendMail(@PathVariable String id) throws IOException {
        return mailService.confirmBooking(id);
    }
}
