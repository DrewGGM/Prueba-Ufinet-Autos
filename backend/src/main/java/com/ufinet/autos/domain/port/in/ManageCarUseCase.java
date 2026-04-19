package com.ufinet.autos.domain.port.in;

import com.ufinet.autos.domain.model.Car;

public interface ManageCarUseCase {

    Car create(Long userId, CreateCarCommand command);

    Car update(Long userId, Long carId, UpdateCarCommand command);

    void delete(Long userId, Long carId);

    Car getById(Long userId, Long carId);

    record CreateCarCommand(
            String brand,
            String model,
            int year,
            String licensePlate,
            String color,
            String photoUrl
    ) {
    }

    record UpdateCarCommand(
            String brand,
            String model,
            int year,
            String licensePlate,
            String color,
            String photoUrl
    ) {
    }
}
