package org.example.service.mapper;

import org.example.api_object.Update;
import org.example.entity.MessageEntity;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class MessageMapper {
    public MessageEntity map(Update update) {
        String chatId = update.getChatId();
        String userId = update.getMessage().getFrom().getId().toString();
        String userFirstname = update.getMessage().getFrom().getFirstName();
        String userNick = update.getMessage().getFrom().getUsername();
        String nationality = update.getMessage().getFrom().getLanguageCode();
        String text = update.getMessage().getText();
        boolean isDocument = false;
        boolean isPhoto = false;
        boolean isAudio = false;
        boolean isAnimation = false;
        boolean isVideo = false;
        String fileId = null;

        if (update.getMessage().getDocument() != null) {
            isDocument = true;
            fileId = update.getMessage().getDocument().getFileId();
            if (update.getMessage().getDocument().getMimeType().startsWith("image"))
                isPhoto = true;
        }

        if (update.getMessage().getAudio() != null) {
            isAudio = true;
            if (fileId == null)
                fileId = update.getMessage().getAudio().getFileId();
        }

        if (update.getMessage().getAnimation() != null) {
            isAnimation = true;
            if (fileId == null)
                fileId = update.getMessage().getAnimation().getFileId();
        }

        if (update.getMessage().getVideo() != null) {
            isVideo = true;
            if (fileId == null)
                fileId = update.getMessage().getVideo().getFileId();
        }

        return MessageEntity.builder()
                .chatId(chatId)
                .userId(userId)
                .userFirstname(userFirstname)
                .userNick(userNick)
                .nationality(nationality)
                .message(text)
                .isDocument(isDocument)
                .isPhoto(isPhoto)
                .isAudio(isAudio)
                .isAnimation(isAnimation)
                .isVideo(isVideo)
                .fileId(fileId)
                .messageDate(OffsetDateTime.now())
                .build();
    }
}
