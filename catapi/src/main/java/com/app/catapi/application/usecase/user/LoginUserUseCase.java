package com.app.catapi.application.usecase.user;

import com.app.catapi.application.dto.user.LoginDto;
import com.app.catapi.domain.entity.user.Token;
import com.app.catapi.domain.ports.AuthenticationPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class LoginUserUseCase {
    private final AuthenticationPort authenticationPort;

    public Token execute(LoginDto loginDto) {
        log.info("LoginUserUseCase execute");

        String token = authenticationPort.authenticate(
                loginDto.getEmail(), loginDto.getPassword());
        return new Token(token);
    }
}
