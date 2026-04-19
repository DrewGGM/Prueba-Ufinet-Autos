package com.ufinet.autos.domain.port.out;

import com.ufinet.autos.domain.model.User;

public interface TokenProviderPort {

    String generateToken(User user);
}
