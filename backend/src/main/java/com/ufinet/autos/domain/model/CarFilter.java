package com.ufinet.autos.domain.model;

public record CarFilter(
        String brand,
        Integer year,
        String search
) {
}
