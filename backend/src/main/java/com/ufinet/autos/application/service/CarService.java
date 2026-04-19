package com.ufinet.autos.application.service;

import com.ufinet.autos.domain.exception.CarNotFoundException;
import com.ufinet.autos.domain.exception.DuplicatePlateException;
import com.ufinet.autos.domain.exception.SoftDeletedPlateException;
import com.ufinet.autos.domain.model.Car;
import com.ufinet.autos.domain.model.CarFilter;
import com.ufinet.autos.domain.model.PageResult;
import com.ufinet.autos.domain.port.in.ManageCarUseCase;
import com.ufinet.autos.domain.port.in.SearchCarUseCase;
import com.ufinet.autos.domain.port.out.CarRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService implements ManageCarUseCase, SearchCarUseCase {

    private final CarRepositoryPort carRepository;

    @Override
    @Transactional
    public Car create(Long userId, CreateCarCommand command) {
        if (carRepository.existsByLicensePlate(command.licensePlate())) {
            throw new DuplicatePlateException(command.licensePlate());
        }
        if (carRepository.existsSoftDeletedByLicensePlate(command.licensePlate())) {
            throw new SoftDeletedPlateException(command.licensePlate());
        }

        Car car = Car.builder()
                .brand(command.brand())
                .model(command.model())
                .year(command.year())
                .licensePlate(command.licensePlate())
                .color(command.color())
                .photoUrl(command.photoUrl())
                .userId(userId)
                .build();

        return carRepository.save(car);
    }

    @Override
    @Transactional
    public Car update(Long userId, Long carId, UpdateCarCommand command) {
        Car existing = carRepository.findByIdAndUserId(carId, userId)
                .orElseThrow(() -> new CarNotFoundException(carId));

        if (carRepository.existsByLicensePlateAndIdNot(command.licensePlate(), carId)) {
            throw new DuplicatePlateException(command.licensePlate());
        }
        if (!command.licensePlate().equals(existing.getLicensePlate())
                && carRepository.existsSoftDeletedByLicensePlate(command.licensePlate())) {
            throw new SoftDeletedPlateException(command.licensePlate());
        }

        existing.setBrand(command.brand());
        existing.setModel(command.model());
        existing.setYear(command.year());
        existing.setLicensePlate(command.licensePlate());
        existing.setColor(command.color());
        existing.setPhotoUrl(command.photoUrl());

        return carRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long carId) {
        Car car = carRepository.findByIdAndUserId(carId, userId)
                .orElseThrow(() -> new CarNotFoundException(carId));
        carRepository.deleteById(car.getId());
    }

    @Override
    public Car getById(Long userId, Long carId) {
        return carRepository.findByIdAndUserId(carId, userId)
                .orElseThrow(() -> new CarNotFoundException(carId));
    }

    @Override
    public PageResult<Car> search(Long userId, CarFilter filter, int page, int size,
                                  String sortBy, String sortDirection) {
        return carRepository.findByUserIdAndFilter(userId, filter, page, size, sortBy, sortDirection);
    }
}
