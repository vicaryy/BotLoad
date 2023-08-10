package org.vicary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vicary.entity.UserEntity;
import org.vicary.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

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

    public Optional<UserEntity> findByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    public Optional<UserEntity> findByUserNick(String nick) {
        return repository.findByNick(nick);
    }

    public boolean updateUserToPremiumByUserId(String userId) {
        UserEntity updatedUser = findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("User by id: " + userId + "does not exists!"));
        updatedUser.setPremium(true);
        saveUser(updatedUser);
        return true;
    }

    public boolean updateUserToPremiumByNick(String nick) {
        UserEntity updatedUser = findByUserNick(nick)
                .orElseThrow(() -> new NoSuchElementException("User by nick: " + nick + " does not exists!"));
        updatedUser.setPremium(true);
        saveUser(updatedUser);
        return true;
    }

    public boolean updateUserToStandardByNick(String nick) {
        UserEntity updatedUser = findByUserNick(nick)
                .orElseThrow(() -> new NoSuchElementException("User by nick: " + nick + "does not exists!"));
        updatedUser.setPremium(false);
        saveUser(updatedUser);
        return true;
    }
}
