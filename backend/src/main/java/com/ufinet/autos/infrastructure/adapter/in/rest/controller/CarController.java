package com.ufinet.autos.infrastructure.adapter.in.rest.controller;

import com.ufinet.autos.domain.model.Car;
import com.ufinet.autos.domain.model.CarFilter;
import com.ufinet.autos.domain.model.PageResult;
import com.ufinet.autos.domain.port.in.ManageCarUseCase;
import com.ufinet.autos.domain.port.in.SearchCarUseCase;
import com.ufinet.autos.infrastructure.adapter.in.rest.dto.CarRequest;
import com.ufinet.autos.infrastructure.adapter.in.rest.dto.CarResponse;
import com.ufinet.autos.infrastructure.adapter.in.rest.dto.PageResponse;
import com.ufinet.autos.infrastructure.adapter.in.rest.mapper.CarRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final ManageCarUseCase manageCarUseCase;
    private final SearchCarUseCase searchCarUseCase;
    private final CarRestMapper mapper;

    @PostMapping
    public ResponseEntity<CarResponse> create(@Valid @RequestBody CarRequest request,
                                              JwtAuthenticationToken auth) {
        Long userId = extractUserId(auth);
        Car car = manageCarUseCase.create(userId, mapper.toCreateCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(car));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getById(@PathVariable Long id,
                                               JwtAuthenticationToken auth) {
        Long userId = extractUserId(auth);
        Car car = manageCarUseCase.getById(userId, id);
        return ResponseEntity.ok(mapper.toResponse(car));
    }

    @GetMapping
    public ResponseEntity<PageResponse<CarResponse>> search(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            JwtAuthenticationToken auth) {

        Long userId = extractUserId(auth);
        CarFilter filter = new CarFilter(brand, year, search);
        PageResult<Car> result = searchCarUseCase.search(userId, filter, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(mapper.toPageResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> update(@PathVariable Long id,
                                              @Valid @RequestBody CarRequest request,
                                              JwtAuthenticationToken auth) {
        Long userId = extractUserId(auth);
        Car car = manageCarUseCase.update(userId, id, mapper.toUpdateCommand(request));
        return ResponseEntity.ok(mapper.toResponse(car));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       JwtAuthenticationToken auth) {
        Long userId = extractUserId(auth);
        manageCarUseCase.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserId(JwtAuthenticationToken auth) {
        return Long.parseLong(auth.getToken().getSubject());
    }
}
