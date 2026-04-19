package com.ufinet.autos.domain.port.in;

import com.ufinet.autos.domain.model.Car;
import com.ufinet.autos.domain.model.CarFilter;
import com.ufinet.autos.domain.model.PageResult;

public interface SearchCarUseCase {

    PageResult<Car> search(Long userId, CarFilter filter, int page, int size,
                           String sortBy, String sortDirection);
}
