package com.ufinet.autos.infrastructure.adapter.in.rest.dto;

public record CarResponse(
        Long id,
        String brand,
        String model,
        Integer year,
        String licensePlate,
        String color,
        String photoUrl
) {
}
