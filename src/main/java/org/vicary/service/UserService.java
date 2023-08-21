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
        Optional<UserEntity> userEntity = findByUserId(userId);
        if (userEntity.isPresent()) {
            return userEntity.get().getAdmin();
        }
        return false;
    }

    public boolean updateUserToPremiumByUserId(String userId) {
        Optional<UserEntity> updatedUser = findByUserId(userId);
        if (updatedUser.isPresent()) {
            updatedUser.get().setPremium(true);
            saveUser(updatedUser.get());
            logger.info("User '{}' updated to Premium.", userId);
            return true;
        }
        return false;
    }

    public boolean updateUserToPremiumByNick(String nick) {
        Optional<UserEntity> updatedUser = findByUserNick(nick);
        if (updatedUser.isPresent()) {
            updatedUser.get().setPremium(true);
            saveUser(updatedUser.get());
            logger.info("User '{}' updated to Premium.", nick);
            return true;
        }
        return false;
    }

    public boolean updateUserToStandardByNick(String nick) {
        Optional<UserEntity> updatedUser = findByUserNick(nick);
        if (updatedUser.isPresent()) {
            updatedUser.get().setPremium(false);
            saveUser(updatedUser.get());
            logger.info("User '{}' updated to Standard.", nick);
            return true;
        }
        return false;
    }

    public boolean updateUserToAdminByNick(String nick) {
        Optional<UserEntity> updatedUser = findByUserNick(nick);
        if (updatedUser.isPresent()) {
            updatedUser.get().setAdmin(true);
            saveUser(updatedUser.get());
            logger.info("User '{}' updated to Admin.", nick);
            return true;
        }
        return false;
    }

    public boolean updateUserToNonAdminByNick(String nick) {
        Optional<UserEntity> updatedUser = findByUserNick(nick);
        if (updatedUser.isPresent()) {
            updatedUser.get().setAdmin(false);
            saveUser(updatedUser.get());
            logger.info("User '{}' updated to Non-Admin.", nick);
            return true;
        }
        return false;
    }
}
