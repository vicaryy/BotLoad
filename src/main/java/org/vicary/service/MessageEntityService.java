package org.vicary.service;

import lombok.RequiredArgsConstructor;
import org.vicary.api_object.Update;
import org.vicary.entity.MessageEntity;
import org.vicary.repository.MessageRepository;
import org.vicary.service.dto.MessageEntityResponse;
import org.vicary.service.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageEntityService {
    private final MessageRepository repository;
    private final MessageMapper mapper;

    public void save(MessageEntity messageEntity) {
        repository.save(messageEntity);
    }

    public Optional<MessageEntity> getMessageById(Long messageId) {
        return repository.findById(messageId);
    }

    public MessageEntityResponse getMessageResponseById(Long messageId) {
        MessageEntity messageEntity = repository
                .findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message by id = " + messageId + " does not exists."));
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
