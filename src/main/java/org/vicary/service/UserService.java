package org.vicary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vicary.entity.UserEntity;
import org.vicary.repository.UserRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public void saveUser(UserEntity userEntity) {
        repository.save(userEntity);
    }

    public boolean existsByUserId(String userId) {
        return repository.existsByUserId(userId);
    }

    public UserEntity findByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    public boolean updateUserToPremiumByUserId(String userId) {
        UserEntity userEntity = repository.findByUserId(userId);
        if (userEntity != null) {
            UserEntity updatedUser = userEntity;
            updatedUser.setPremium(true);
            repository.save(updatedUser);
            return true;
        }
        throw new NoSuchElementException("User by id: " + userId + "does not exists!");
    }

    public boolean updateUserToPremiumByNick(String nick) {
        UserEntity userEntity = repository.findByNick(nick);
        if (userEntity != null) {
            UserEntity updatedUser = userEntity;
            updatedUser.setPremium(true);
            repository.save(updatedUser);
            return true;
        }
        throw new NoSuchElementException("User by id: " + nick + "does not exists!");
    }

    public boolean updateUserToStandardByNick(String nick) {
        UserEntity userEntity = repository.findByNick(nick);
        if (userEntity != null) {
            UserEntity updatedUser = userEntity;
            updatedUser.setPremium(false);
            repository.save(updatedUser);
            return true;
        }
        throw new NoSuchElementException("User by id: " + nick + "does not exists!");
    }
}
