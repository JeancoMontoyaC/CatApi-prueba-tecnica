package com.app.catapi.infrastructure.controller;

import com.app.catapi.application.dto.user.LoginDto;
import com.app.catapi.application.dto.user.TokenDto;
import com.app.catapi.application.usecase.user.LoginUserUseCase;
import com.app.catapi.application.usecase.user.RegisterUserUseCase;
import com.app.catapi.domain.entity.user.Token;
import com.app.catapi.domain.entity.user.User;
import com.app.catapi.infrastructure.mapper.TokenMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "User registration and login endpoints. No JWT token required")

public class UserController {
    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final TokenMapper tokenMapper;

    @Operation(summary = "Login", description = "Authenticates a user with email and password and returns a JWT token")
    @PostMapping("/auth/login")
    public ResponseEntity<TokenDto> loginUser(@RequestBody @Valid LoginDto loginDto) {
        log.info("loginUser into server");

        Token token = loginUserUseCase.execute(loginDto);

        TokenDto tokenDto = tokenMapper.toTokenDto(token);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/auth/register")
    @Operation(summary = "Register", description = "Creates a new user account and returns a JWT token. All fields are required, email must be valid and password must be at least 8 characters")
    public ResponseEntity<TokenDto> registerUser(@RequestBody @Valid User user) {

        log.info("RegisterUser into server");
        Token token = registerUserUseCase.execute(user);
        TokenDto tokenDto = tokenMapper.toTokenDto(token);
        return ResponseEntity.ok(tokenDto);
    }
}
