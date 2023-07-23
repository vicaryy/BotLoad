package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.api_object.Update;
import org.example.entity.MessageEntity;
import org.example.repository.MessageRepository;
import org.example.service.mapper.MessageMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageEntityService {
    private final MessageRepository repository;
    private final MessageMapper mapper;

    public void save(Update update) {
        MessageEntity messageEntity = mapper.map(update);
        repository.save(messageEntity);
    }
}
