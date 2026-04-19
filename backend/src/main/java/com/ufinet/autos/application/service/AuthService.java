package com.ufinet.autos.application.service;

import com.ufinet.autos.domain.exception.InvalidCredentialsException;
import com.ufinet.autos.domain.exception.UserAlreadyExistsException;
import com.ufinet.autos.domain.model.AuthResult;
import com.ufinet.autos.domain.model.User;
import com.ufinet.autos.domain.port.in.AuthenticateUserUseCase;
import com.ufinet.autos.domain.port.in.RegisterUserUseCase;
import com.ufinet.autos.domain.port.out.PasswordEncoderPort;
import com.ufinet.autos.domain.port.out.TokenProviderPort;
import com.ufinet.autos.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService implements RegisterUserUseCase, AuthenticateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenProviderPort tokenProvider;

    @Override
    @Transactional
    public AuthResult register(RegisterCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new UserAlreadyExistsException(command.email());
        }

        User user = User.builder()
                .email(command.email())
                .password(passwordEncoder.encode(command.password()))
                .build();

        User saved = userRepository.save(user);
        String token = tokenProvider.generateToken(saved);

        return new AuthResult(token, saved.getEmail());
    }

    @Override
    public AuthResult authenticate(LoginCommand command) {
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(command.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = tokenProvider.generateToken(user);
        return new AuthResult(token, user.getEmail());
    }
}
