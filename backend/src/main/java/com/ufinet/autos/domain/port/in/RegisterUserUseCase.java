package com.ufinet.autos.domain.port.in;

import com.ufinet.autos.domain.model.AuthResult;

public interface RegisterUserUseCase {

    AuthResult register(RegisterCommand command);

    record RegisterCommand(String email, String password) {
    }
}
