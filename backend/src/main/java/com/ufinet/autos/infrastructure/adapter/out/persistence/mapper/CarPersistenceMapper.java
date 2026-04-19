package com.ufinet.autos.infrastructure.adapter.out.persistence.mapper;

import com.ufinet.autos.domain.model.Car;
import com.ufinet.autos.infrastructure.adapter.out.persistence.entity.CarJpaEntity;
import com.ufinet.autos.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CarPersistenceMapper {

    public Car toDomain(CarJpaEntity entity) {
        return Car.builder()
                .id(entity.getId())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .year(entity.getYear())
                .licensePlate(entity.getLicensePlate())
                .color(entity.getColor())
                .photoUrl(entity.getPhotoUrl())
                .userId(entity.getUser().getId())
                .build();
    }

    public CarJpaEntity toEntity(Car domain, UserJpaEntity userRef) {
        return CarJpaEntity.builder()
                .id(domain.getId())
                .brand(domain.getBrand())
                .model(domain.getModel())
                .year(domain.getYear())
                .licensePlate(domain.getLicensePlate())
                .color(domain.getColor())
                .photoUrl(domain.getPhotoUrl())
                .user(userRef)
                .build();
    }
}
