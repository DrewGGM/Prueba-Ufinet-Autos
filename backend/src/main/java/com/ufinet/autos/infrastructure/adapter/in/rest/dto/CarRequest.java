package com.ufinet.autos.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CarRequest(
        @NotBlank(message = "La marca es obligatoria")
        @Size(max = 100, message = "La marca no puede exceder 100 caracteres")
        String brand,

        @NotBlank(message = "El modelo es obligatorio")
        @Size(max = 100, message = "El modelo no puede exceder 100 caracteres")
        String model,

        @Min(value = 1900, message = "El ano debe ser mayor o igual a 1900")
        @Max(value = 2100, message = "El ano debe ser menor o igual a 2100")
        int year,

        @NotBlank(message = "La placa es obligatoria")
        @Size(max = 20, message = "La placa no puede exceder 20 caracteres")
        String licensePlate,

        @NotBlank(message = "El color es obligatorio")
        @Size(max = 50, message = "El color no puede exceder 50 caracteres")
        String color,

        @Size(max = 500, message = "La URL de la foto no puede exceder 500 caracteres")
        String photoUrl
) {
}
