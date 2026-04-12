package com.app.catapi.infrastructure.database.mapper;

import com.app.catapi.domain.entity.user.User;
import com.app.catapi.domain.entity.user.UserRole;
import com.app.catapi.infrastructure.dataBase.entity.UserEntity;
import com.app.catapi.infrastructure.dataBase.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;


    @ExtendWith(MockitoExtension.class)
    class UserMapperTest {

        @InjectMocks
        private UserMapper userMapper;

        private User buildUser() {
            return User.builder()
                    .email("john@mail.com")
                    .password("encodedPassword")
                    .role(UserRole.USER)
                    .firstName("John")
                    .lastName("Doe")
                    .build();
        }

        private UserEntity buildUserEntity() {
            return UserEntity.builder()
                    .email("john@mail.com")
                    .password("encodedPassword")
                    .role(UserRole.USER)
                    .firstName("John")
                    .lastName("Doe")
                    .build();
        }

        @Test
        void toUserEntity_shouldMapAllFields() {
            User user = buildUser();
            UserEntity result = userMapper.toUserEntity(user);

            assertThat(result.getEmail()).isEqualTo(user.getEmail());
            assertThat(result.getPassword()).isEqualTo(user.getPassword());
            assertThat(result.getRole()).isEqualTo(user.getRole());
            assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
            assertThat(result.getLastName()).isEqualTo(user.getLastName());
        }

        @Test
        void toUser_shouldMapAllFields() {
            UserEntity userEntity = buildUserEntity();
            User result = userMapper.toUser(userEntity);

            assertThat(result.getEmail()).isEqualTo(userEntity.getEmail());
            assertThat(result.getPassword()).isEqualTo(userEntity.getPassword());
            assertThat(result.getRole()).isEqualTo(userEntity.getRole());
            assertThat(result.getFirstName()).isEqualTo(userEntity.getFirstName());
            assertThat(result.getLastName()).isEqualTo(userEntity.getLastName());
        }

}
