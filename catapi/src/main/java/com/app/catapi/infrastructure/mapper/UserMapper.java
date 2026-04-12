package com.app.catapi.infrastructure.mapper;

import com.app.catapi.domain.entity.user.User;
import com.app.catapi.infrastructure.dataBase.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {
    public UserEntity toUserEntity(User user) {
        return UserEntity.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public User toUser(UserEntity user) {
        return User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
