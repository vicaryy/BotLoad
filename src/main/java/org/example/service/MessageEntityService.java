package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.api_object.Update;
import org.example.entity.MessageEntity;
import org.example.repository.MessageEntityRepository;
import org.example.service.dto.MessageEntityResponse;
import org.example.service.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageEntityService {
    private final MessageEntityRepository repository;
    private final MessageMapper mapper;

    public void save(Update update) {
        MessageEntity messageEntity = mapper.map(update);
        repository.save(messageEntity);
    }

    public MessageEntityResponse getMessageById(Long messageId) {
        MessageEntity messageEntity = repository.findById(messageId).get();
        return mapper.map(messageEntity);
    }

    public List<MessageEntityResponse> getMessagesByUserId(String userId) {
        List<MessageEntity> messageEntities = repository.findAllByUserId(userId);
        List<MessageEntityResponse> messageEntityResponses = mapper.map(messageEntities);
        return messageEntityResponses;
    }

    public List<MessageEntityResponse> getAllMessages() {
        List<MessageEntity> messageEntities = repository.findAll();
        List<MessageEntityResponse> messageEntityResponses = mapper.map(messageEntities);
        return messageEntityResponses;
    }
}
