package com.autonoleggio.booking;

import com.autonoleggio.security.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Booking {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private User user;

    @NotBlank
    @Size(min = 1, max = 50)
    private String carName;

    private Date bookingDate;

    @Enumerated(EnumType.STRING)
    private BookingCity pickUpLocation;

    @Enumerated(EnumType.STRING)
    private BookingCity dropOfLocation;

    @Size(min = 10, max = 10, message = "Inserisci una data di ritiro valida")
    @NotBlank
    @Column(length = 10)
    private String pickUpDate;

    @Size(min = 10, max = 10, message = "Inserisci una data di riconsegna valida")
    @NotBlank
    @Column(length = 10, nullable = false)
    private String dropOfDate;

    @Size(min = 5, max = 5, message = "Inserisci un orario di ritiro valido")
    @NotBlank
    @Column(length = 5, nullable = false)
    private String pickUpHour;

    @Size(min = 5, max = 5, message = "Inserisci un orario di riconsegna valido")
    @NotBlank
    @Column(length = 5, nullable = false)
    private String dropOfHour;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private Long totalPrice;
}
