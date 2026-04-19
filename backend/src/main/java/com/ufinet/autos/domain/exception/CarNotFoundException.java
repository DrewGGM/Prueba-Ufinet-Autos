package com.ufinet.autos.domain.exception;

public class CarNotFoundException extends RuntimeException {

    public CarNotFoundException(Long carId) {
        super("Auto no encontrado con id: " + carId);
    }
}
