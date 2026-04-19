package com.ufinet.autos.domain.port.out;

import com.ufinet.autos.domain.model.Car;
import com.ufinet.autos.domain.model.CarFilter;
import com.ufinet.autos.domain.model.PageResult;

import java.util.Optional;

public interface CarRepositoryPort {

    Car save(Car car);

    Optional<Car> findByIdAndUserId(Long id, Long userId);

    PageResult<Car> findByUserIdAndFilter(Long userId, CarFilter filter,
                                          int page, int size,
                                          String sortBy, String sortDirection);

    boolean existsByLicensePlate(String licensePlate);

    boolean existsByLicensePlateAndIdNot(String licensePlate, Long id);

    boolean existsSoftDeletedByLicensePlate(String licensePlate);

    void deleteById(Long id);
}
