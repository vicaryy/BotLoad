package org.vicary.repository;

import org.vicary.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserId(String userId);
    UserEntity findByNick(String nick);
    boolean existsByUserId(String userId);
}
