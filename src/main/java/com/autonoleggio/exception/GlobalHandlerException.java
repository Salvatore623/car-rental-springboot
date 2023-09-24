package com.autonoleggio.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<String> handler(EntityNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(BookingStatusNotChangeable.class)
    ResponseEntity<String> handler(BookingStatusNotChangeable e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(EntityExistsException.class)
    ResponseEntity<String> handler(EntityExistsException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handler(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<String> handler(AccessDeniedException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> handler(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<String[]> handler(MethodArgumentNotValidException e){
        String[] errorMessages = e.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toArray(String[]::new);
        return ResponseEntity.badRequest().body(errorMessages);
    }




}
