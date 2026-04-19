package com.ufinet.autos.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private String licensePlate;
    private String color;
    private String photoUrl;
    private Long userId;
}
