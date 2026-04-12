package com.app.catapi.domain.ports;

import com.app.catapi.domain.entity.user.User;

public interface AuthenticationPort {
    String authenticate(String email, String password);
}
