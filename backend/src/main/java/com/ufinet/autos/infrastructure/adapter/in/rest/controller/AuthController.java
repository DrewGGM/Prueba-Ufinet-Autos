package com.ufinet.autos.infrastructure.adapter.in.rest.controller;

import com.ufinet.autos.domain.model.AuthResult;
import com.ufinet.autos.domain.port.in.AuthenticateUserUseCase;
import com.ufinet.autos.domain.port.in.AuthenticateUserUseCase.LoginCommand;
import com.ufinet.autos.domain.port.in.RegisterUserUseCase;
import com.ufinet.autos.domain.port.in.RegisterUserUseCase.RegisterCommand;
import com.ufinet.autos.infrastructure.adapter.in.rest.dto.AuthResponse;
import com.ufinet.autos.infrastructure.adapter.in.rest.dto.LoginRequest;
import com.ufinet.autos.infrastructure.adapter.in.rest.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUseCase;
    private final AuthenticateUserUseCase authenticateUseCase;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResult result = registerUseCase.register(
                new RegisterCommand(request.email(), request.password())
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(result.token(), result.email()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResult result = authenticateUseCase.authenticate(
                new LoginCommand(request.email(), request.password())
        );
        return ResponseEntity.ok(new AuthResponse(result.token(), result.email()));
    }
}
