package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.MessageEntityService;
import org.example.service.dto.MessageEntityResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageEntityController {
    private final MessageEntityService service;

    @GetMapping(path = "/api/message/{messageId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MessageEntityResponse getMessageByMessageId(@PathVariable Long messageId) {
        return service.getMessageById(messageId);
    }
    @GetMapping(path = "/api/message")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<MessageEntityResponse> getMessagesByUserId(@RequestParam String userId) {
        return service.getMessagesByUserId(userId);
    }
    @GetMapping(path = "/api/message/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<MessageEntityResponse> getAllMessages() {
        return service.getAllMessages();
    }
}
