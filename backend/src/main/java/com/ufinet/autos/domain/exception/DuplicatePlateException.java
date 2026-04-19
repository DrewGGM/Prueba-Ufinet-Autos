package com.ufinet.autos.domain.exception;

public class DuplicatePlateException extends RuntimeException {

    public DuplicatePlateException(String licensePlate) {
        super("Ya existe un auto con la placa: " + licensePlate);
    }
}
