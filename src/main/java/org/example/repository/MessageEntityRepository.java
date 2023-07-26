package org.example.repository;

import org.example.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageEntityRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findAllByUserId(String userId);
}
