package com.autonoleggio.exception;

public class BookingStatusNotChangeable extends RuntimeException {
    public BookingStatusNotChangeable(String message){
        super(message);
    }
}
