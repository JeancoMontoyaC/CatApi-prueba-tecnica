package com.app.catapi.infrastructure.authentication;

import com.app.catapi.domain.entity.user.User;
import com.app.catapi.domain.ports.AuthenticationPort;
import com.app.catapi.infrastructure.dataBase.entity.UserEntity;
import com.app.catapi.infrastructure.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationImpl implements AuthenticationPort {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Override
    public String authenticate(String email, String password) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email, password
                )
        );

        UserEntity user = (UserEntity) authenticate.getPrincipal();

        return jwtService.generateToken(user);
    }
}
