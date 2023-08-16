package org.vicary.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vicary.entity.UserEntity;
import org.vicary.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

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

    public boolean isUserAdmin(String userId) {
        UserEntity userEntity = findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("User by id: " + userId + " does not exists!"));
        return userEntity.getAdmin();
    }

    public boolean updateUserToPremiumByUserId(String userId) {
        UserEntity updatedUser = findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("User by id: " + userId + " does not exists!"));
        updatedUser.setPremium(true);
        saveUser(updatedUser);
        logger.info("User '{}' updated to Premium.", userId);
        return true;
    }

    public boolean updateUserToPremiumByNick(String nick) {
        UserEntity updatedUser = findByUserNick(nick)
                .orElseThrow(() -> new NoSuchElementException("User by nick: " + nick + " does not exists!"));
        updatedUser.setPremium(true);
        saveUser(updatedUser);
        logger.info("User '{}' updated to Premium.", nick);
        return true;
    }

    public boolean updateUserToStandardByNick(String nick) {
        UserEntity updatedUser = findByUserNick(nick)
                .orElseThrow(() -> new NoSuchElementException("User by nick: " + nick + " does not exists!"));
        updatedUser.setPremium(false);
        saveUser(updatedUser);
        logger.info("User '{}' updated to Standard.", nick);
        return true;
    }

    public boolean updateUserToAdminByNick(String nick) {
        UserEntity updatedUser = findByUserNick(nick)
                .orElseThrow(() -> new NoSuchElementException("User by nick: " + nick + " does not exists!"));
        updatedUser.setAdmin(true);
        saveUser(updatedUser);
        logger.info("User '{}' updated to Admin.", nick);
        return true;
    }

    public boolean updateUserToNonAdminByNick(String nick) {
        UserEntity updatedUser = findByUserNick(nick)
                .orElseThrow(() -> new NoSuchElementException("User by nick: " + nick + " does not exists!"));
        updatedUser.setAdmin(false);
        saveUser(updatedUser);
        logger.info("User '{}' updated to Non-Admin.", nick);
        return true;
    }
}
