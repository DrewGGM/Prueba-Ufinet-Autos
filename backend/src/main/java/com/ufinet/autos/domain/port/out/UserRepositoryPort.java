package com.ufinet.autos.domain.port.out;

import com.ufinet.autos.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
