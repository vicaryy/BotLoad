package org.vicary.repository;

import org.springframework.data.jpa.repository.Query;
import org.vicary.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    @Query (name = "SELECT FROM MESSAGES WHERE USER_ID = :userId")
    List<MessageEntity> findAllByUserId(String userId);
}
