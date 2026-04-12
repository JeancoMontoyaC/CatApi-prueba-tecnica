package com.app.catapi.infrastructure.controller;

import com.app.catapi.application.dto.user.LoginDto;
import com.app.catapi.application.dto.user.TokenDto;
import com.app.catapi.application.usecase.user.LoginUserUseCase;
import com.app.catapi.application.usecase.user.RegisterUserUseCase;
import com.app.catapi.domain.entity.user.Token;
import com.app.catapi.domain.entity.user.User;
import com.app.catapi.infrastructure.dataBase.mapper.TokenMapper;
import com.app.catapi.infrastructure.security.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final TokenMapper tokenMapper;

    @PostMapping("/auth/login")
    public ResponseEntity<TokenDto> loginUser(@RequestBody LoginDto loginDto) {
        log.info("loginUser into server");

        Token token = loginUserUseCase.execute(loginDto);

        TokenDto tokenDto = tokenMapper.toTokenDto(token);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<TokenDto> registerUser(@RequestBody User user) {

        Token token = registerUserUseCase.execute(user);
        TokenDto tokenDto = tokenMapper.toTokenDto(token);
        return ResponseEntity.ok(tokenDto);
    }
}
