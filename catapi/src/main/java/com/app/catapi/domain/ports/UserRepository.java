package com.app.catapi.domain.ports;

import com.app.catapi.application.dto.user.LoginDto;
import com.app.catapi.domain.entity.user.Token;
import com.app.catapi.domain.entity.user.User;

import java.util.Optional;

public interface UserRepository {
    boolean existByEmail(String email);
    void registerUser(User user);
    Optional<User> findByEmail(String email);
}
