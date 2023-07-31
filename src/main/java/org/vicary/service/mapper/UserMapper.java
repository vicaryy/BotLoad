package org.vicary.service.mapper;

import org.vicary.api_object.User;
import org.vicary.entity.UserEntity;
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
