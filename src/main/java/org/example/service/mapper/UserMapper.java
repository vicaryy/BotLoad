package org.example.service.mapper;

import org.example.api_object.User;
import org.example.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity map(User user) {
        return UserEntity.builder()
                .nick(user.getUsername())
                .nationality(user.getLanguageCode())
                .premium(false)
                .admin(false)
                .userId(user.getId().toString())
                .build();
    }
}
