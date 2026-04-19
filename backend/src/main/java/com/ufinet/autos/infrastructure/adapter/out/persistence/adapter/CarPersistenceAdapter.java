package com.ufinet.autos.infrastructure.adapter.out.persistence.adapter;

import com.ufinet.autos.domain.model.Car;
import com.ufinet.autos.domain.model.CarFilter;
import com.ufinet.autos.domain.model.PageResult;
import com.ufinet.autos.domain.port.out.CarRepositoryPort;
import com.ufinet.autos.infrastructure.adapter.out.persistence.entity.CarJpaEntity;
import com.ufinet.autos.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.ufinet.autos.infrastructure.adapter.out.persistence.mapper.CarPersistenceMapper;
import com.ufinet.autos.infrastructure.adapter.out.persistence.repository.CarJpaRepository;
import com.ufinet.autos.infrastructure.adapter.out.persistence.specification.CarSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CarPersistenceAdapter implements CarRepositoryPort {

    private final CarJpaRepository jpaRepository;
    private final CarPersistenceMapper mapper;

    @Override
    public Car save(Car car) {
        UserJpaEntity userRef = UserJpaEntity.builder().id(car.getUserId()).build();
        CarJpaEntity entity = mapper.toEntity(car, userRef);
        CarJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Car> findByIdAndUserId(Long id, Long userId) {
        return jpaRepository.findByIdAndUserId(id, userId)
                .map(mapper::toDomain);
    }

    @Override
    public PageResult<Car> findByUserIdAndFilter(Long userId, CarFilter filter,
                                                 int page, int size,
                                                 String sortBy, String sortDirection) {
        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy != null ? sortBy : "id"
        );

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Specification<CarJpaEntity> spec = CarSpecification.fromFilter(userId, filter);

        Page<CarJpaEntity> result = jpaRepository.findAll(spec, pageRequest);

        return new PageResult<>(
                result.getContent().stream().map(mapper::toDomain).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast()
        );
    }

    @Override
    public boolean existsByLicensePlate(String licensePlate) {
        return jpaRepository.existsByLicensePlate(licensePlate);
    }

    @Override
    public boolean existsByLicensePlateAndIdNot(String licensePlate, Long id) {
        return jpaRepository.existsByLicensePlateAndIdNot(licensePlate, id);
    }

    @Override
    public boolean existsSoftDeletedByLicensePlate(String licensePlate) {
        return jpaRepository.existsSoftDeletedByLicensePlate(licensePlate);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.softDeleteById(id);
    }
}
