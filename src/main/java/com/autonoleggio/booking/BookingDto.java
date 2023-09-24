package com.autonoleggio.booking;

import lombok.Builder;

import java.util.Date;

@Builder
public record BookingDto(
        Integer id,
        String carName,
        Date bookingDate,
        BookingStatus status,
        BookingCity pickUpLocation,
        BookingCity dropOfLocation,
        String pickUpDate,
        String dropOfDate,
        String pickUpHour,
        String dropOfHour,
        Long totalPrice
) {
}
