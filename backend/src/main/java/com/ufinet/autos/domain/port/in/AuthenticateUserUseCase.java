package com.ufinet.autos.domain.port.in;

import com.ufinet.autos.domain.model.AuthResult;

public interface AuthenticateUserUseCase {

    AuthResult authenticate(LoginCommand command);

    record LoginCommand(String email, String password) {
    }
}
