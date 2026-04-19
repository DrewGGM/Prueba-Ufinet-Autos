package com.ufinet.autos.infrastructure.adapter.in.rest.mapper;

import com.ufinet.autos.domain.model.Car;
import com.ufinet.autos.domain.model.PageResult;
import com.ufinet.autos.domain.port.in.ManageCarUseCase.CreateCarCommand;
import com.ufinet.autos.domain.port.in.ManageCarUseCase.UpdateCarCommand;
import com.ufinet.autos.infrastructure.adapter.in.rest.dto.CarRequest;
import com.ufinet.autos.infrastructure.adapter.in.rest.dto.CarResponse;
import com.ufinet.autos.infrastructure.adapter.in.rest.dto.PageResponse;
import org.springframework.stereotype.Component;

@Component
public class CarRestMapper {

    public CreateCarCommand toCreateCommand(CarRequest request) {
        return new CreateCarCommand(
                request.brand(),
                request.model(),
                request.year(),
                request.licensePlate(),
                request.color(),
                request.photoUrl()
        );
    }

    public UpdateCarCommand toUpdateCommand(CarRequest request) {
        return new UpdateCarCommand(
                request.brand(),
                request.model(),
                request.year(),
                request.licensePlate(),
                request.color(),
                request.photoUrl()
        );
    }

    public CarResponse toResponse(Car car) {
        return new CarResponse(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getLicensePlate(),
                car.getColor(),
                car.getPhotoUrl()
        );
    }

    public PageResponse<CarResponse> toPageResponse(PageResult<Car> page) {
        return new PageResponse<>(
                page.content().stream().map(this::toResponse).toList(),
                page.page(),
                page.size(),
                page.totalElements(),
                page.totalPages(),
                page.last()
        );
    }
}
