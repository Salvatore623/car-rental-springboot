package com.autonoleggio.car;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Car {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String imgName;
    private String mark;
    private String model;
    private String year;
    private String doors;
    private boolean ac;

    @Enumerated(EnumType.STRING)
    private Transmission transmission;

    @Enumerated(EnumType.STRING)
    private Fuel fuel;

    private Integer pricePerDay;
}
