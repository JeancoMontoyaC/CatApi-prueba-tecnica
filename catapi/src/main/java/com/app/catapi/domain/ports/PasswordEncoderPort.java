package com.app.catapi.domain.ports;

public interface PasswordEncoderPort {
    String encode(String password);
}
