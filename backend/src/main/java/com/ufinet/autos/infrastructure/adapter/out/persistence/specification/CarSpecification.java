package com.ufinet.autos.infrastructure.adapter.out.persistence.specification;

import com.ufinet.autos.domain.model.CarFilter;
import com.ufinet.autos.infrastructure.adapter.out.persistence.entity.CarJpaEntity;
import org.springframework.data.jpa.domain.Specification;

public final class CarSpecification {

    private CarSpecification() {
    }

    public static Specification<CarJpaEntity> belongsToUser(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<CarJpaEntity> hasBrand(String brand) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("brand")), "%" + brand.toLowerCase() + "%");
    }

    public static Specification<CarJpaEntity> hasYear(Integer year) {
        return (root, query, cb) -> cb.equal(root.get("year"), year);
    }

    public static Specification<CarJpaEntity> searchByPlateOrModel(String search) {
        return (root, query, cb) -> {
            String pattern = "%" + search.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("licensePlate")), pattern),
                    cb.like(cb.lower(root.get("model")), pattern)
            );
        };
    }

    public static Specification<CarJpaEntity> fromFilter(Long userId, CarFilter filter) {
        Specification<CarJpaEntity> spec = belongsToUser(userId);

        if (filter == null) {
            return spec;
        }

        if (filter.brand() != null && !filter.brand().isBlank()) {
            spec = spec.and(hasBrand(filter.brand()));
        }

        if (filter.year() != null) {
            spec = spec.and(hasYear(filter.year()));
        }

        if (filter.search() != null && !filter.search().isBlank()) {
            spec = spec.and(searchByPlateOrModel(filter.search()));
        }

        return spec;
    }
}
