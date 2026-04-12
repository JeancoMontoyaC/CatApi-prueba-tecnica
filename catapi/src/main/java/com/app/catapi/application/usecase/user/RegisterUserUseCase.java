package com.app.catapi.application.usecase.user;

import com.app.catapi.application.dto.user.UserDto;
import com.app.catapi.domain.entity.user.Token;
import com.app.catapi.domain.entity.user.User;
import com.app.catapi.domain.entity.user.UserRole;
import com.app.catapi.domain.exception.EmailAlreadyUsedException;
import com.app.catapi.domain.ports.AuthenticationPort;
import com.app.catapi.domain.ports.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class RegisterUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationPort authenticationPort;

    public Token execute(UserDto userDto) {
        log.info("Received request to register user with email {}", userDto.getEmail());

        boolean existByEmail = userRepository.existByEmail(userDto.getEmail());

        if (existByEmail) {
            throw new EmailAlreadyUsedException("Email already used");
        }

        String encoded =  passwordEncoder.encode(userDto.getPassword());

        User user = User.builder()
                .firstName(userDto.getFirstName())
                .role(UserRole.USER)
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(encoded)
                .build();

        userRepository.registerUser(user);

        String token = authenticationPort.authenticate(userDto.getEmail(), userDto.getPassword());
        return new Token(token);
    }
}
