package com.app.catapi.application.usecase.user;

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

    public Token execute(User user) {
        log.info("Received request to register user with email {}", user.getEmail());

        boolean existByEmail = userRepository.existByEmail(user.getEmail());

        if (existByEmail) {
            throw new EmailAlreadyUsedException("Email already used");
        }

        String encoded =  passwordEncoder.encode(user.getPassword());

        User userToSave = User.builder()
                .firstName(user.getFirstName())
                .role(UserRole.USER)
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(encoded)
                .build();

        userRepository.registerUser(userToSave);

        String token = authenticationPort.authenticate(user.getEmail(), user.getPassword());
        return new Token(token);
    }
}
