package com.ufinet.autos.domain.exception;

public class SoftDeletedPlateException extends RuntimeException {

    public SoftDeletedPlateException(String licensePlate) {
        super("La placa " + licensePlate + " pertenece a un auto eliminado previamente");
    }
}
