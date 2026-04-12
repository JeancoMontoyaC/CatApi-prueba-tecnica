package com.app.catapi.infrastructure.dataBase.mapper;

import com.app.catapi.application.dto.user.TokenDto;
import com.app.catapi.domain.entity.user.Token;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TokenMapper {

    public TokenDto toTokenDto(Token token) {
        return TokenDto.builder()
                .token(token.getToken())
                .build();
    }
}
