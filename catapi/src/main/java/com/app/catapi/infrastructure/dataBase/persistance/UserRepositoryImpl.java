package com.app.catapi.infrastructure.dataBase.persistance;

import com.app.catapi.domain.entity.user.User;
import com.app.catapi.domain.ports.UserRepository;
import com.app.catapi.infrastructure.mapper.UserMapper;
import com.app.catapi.infrastructure.dataBase.repository.UserJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;


    @Override
    public boolean existByEmail(String email) {
        return userJpaRepository.findByEmail(email).isPresent();
    }

    @Override
    public void registerUser(User user) {
        userJpaRepository.save(userMapper.toUserEntity(user));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(userMapper::toUser);
    }
}
