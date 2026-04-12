package com.app.catapi.application.usecase;

import com.app.catapi.application.dto.user.LoginDto;
import com.app.catapi.application.usecase.user.LoginUserUseCase;
import com.app.catapi.domain.entity.user.Token;
import com.app.catapi.domain.ports.AuthenticationPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    @InjectMocks
    private LoginUserUseCase loginUserUseCase;

    @Mock
    private AuthenticationPort authenticationPort;

    @Test
    void execute_shouldReturnToken_whenCredentialsAreValid() {
        LoginDto loginDto = new LoginDto("john@mail.com", "password");
        when(authenticationPort.authenticate("john@mail.com", "password")).thenReturn("jwt-token");

        Token token = loginUserUseCase.execute(loginDto);

        assertThat(token.getToken()).isEqualTo("jwt-token");
    }

    @Test
    void execute_shouldCallAuthenticate_withCorrectCredentials() {
        LoginDto loginDto = new LoginDto("john@mail.com", "password");
        when(authenticationPort.authenticate(any(), any())).thenReturn("jwt-token");

        loginUserUseCase.execute(loginDto);

        verify(authenticationPort).authenticate("john@mail.com", "password");
    }

    @Test
    void execute_shouldThrow_whenAuthenticationFails() {
        LoginDto loginDto = new LoginDto("john@mail.com", "wrongPassword");
        when(authenticationPort.authenticate(any(), any())).thenThrow(new RuntimeException("Bad credentials"));

        assertThatThrownBy(() -> loginUserUseCase.execute(loginDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Bad credentials");
    }
}