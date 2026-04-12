package com.app.catapi.application.usecase;

import com.app.catapi.application.dto.user.UserDto;
import com.app.catapi.application.usecase.user.RegisterUserUseCase;
import com.app.catapi.domain.entity.user.Token;
import com.app.catapi.domain.entity.user.User;
import com.app.catapi.domain.entity.user.UserRole;
import com.app.catapi.domain.ports.AuthenticationPort;
import com.app.catapi.domain.ports.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationPort authenticationPort;

    private UserDto buildUser() {
        return UserDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@mail.com")
                .password("rawPassword")
                .build();
    }

    @Test
    void execute_shouldReturnToken_whenUserIsNew() {
        UserDto user = buildUser();

        when(userRepository.existByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(authenticationPort.authenticate(user.getEmail(), user.getPassword())).thenReturn("jwt-token");

        Token token = registerUserUseCase.execute(user);

        assertThat(token.getToken()).isEqualTo("jwt-token");
    }

    @Test
    void execute_shouldThrow_whenEmailAlreadyExists() {
        UserDto user = buildUser();

        when(userRepository.existByEmail(user.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> registerUserUseCase.execute(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email already used");

        verify(userRepository, never()).registerUser(any());
        verify(authenticationPort, never()).authenticate(any(), any());
    }

    @Test
    void execute_shouldEncodePassword_beforeSaving() {
        UserDto user = buildUser();

        when(userRepository.existByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(authenticationPort.authenticate(any(), any())).thenReturn("jwt-token");

        registerUserUseCase.execute(user);

        verify(userRepository).registerUser(argThat(saved ->
                saved.getPassword().equals("encodedPassword")
        ));
    }

    @Test
    void execute_shouldSaveUser_withRoleUser() {
        UserDto user = buildUser();

        when(userRepository.existByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(authenticationPort.authenticate(any(), any())).thenReturn("jwt-token");

        registerUserUseCase.execute(user);

        verify(userRepository).registerUser(argThat(saved ->
                saved.getRole().equals(UserRole.USER)
        ));
    }

    @Test
    void execute_shouldCallAuthenticate_withOriginalPassword() {
        UserDto user = buildUser();

        when(userRepository.existByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(authenticationPort.authenticate(any(), any())).thenReturn("jwt-token");

        registerUserUseCase.execute(user);

        verify(authenticationPort).authenticate("john@mail.com", "rawPassword");
    }
}