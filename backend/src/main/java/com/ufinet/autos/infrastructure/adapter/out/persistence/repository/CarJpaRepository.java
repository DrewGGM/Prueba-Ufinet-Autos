package com.ufinet.autos.infrastructure.adapter.out.persistence.repository;

import com.ufinet.autos.infrastructure.adapter.out.persistence.entity.CarJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CarJpaRepository extends JpaRepository<CarJpaEntity, Long>,
        JpaSpecificationExecutor<CarJpaEntity> {

    Optional<CarJpaEntity> findByIdAndUserId(Long id, Long userId);

    boolean existsByLicensePlate(String licensePlate);

    boolean existsByLicensePlateAndIdNot(String licensePlate, Long id);

    @Query(value = "SELECT CAST(CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END AS BIT) FROM cars WHERE license_plate = :licensePlate AND deleted = 1",
            nativeQuery = true)
    boolean existsSoftDeletedByLicensePlate(String licensePlate);

    @Modifying
    @Query("UPDATE CarJpaEntity c SET c.deleted = true, c.updatedAt = CURRENT_TIMESTAMP WHERE c.id = :id")
    void softDeleteById(Long id);
}
