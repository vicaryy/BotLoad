package org.vicary.service.mapper;

import lombok.RequiredArgsConstructor;
import org.vicary.DataTime;
import org.vicary.api_object.Update;
import org.vicary.entity.MessageEntity;
import org.vicary.service.dto.MessageEntityResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final DataTime dataTime;
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
                .messageDate(dataTime.now())
                .build();
    }

    public MessageEntityResponse map(MessageEntity messageEntity) {
        return MessageEntityResponse.builder()
                .messageId(messageEntity.getMessageId())
                .chatId(messageEntity.getChatId())
                .userId(messageEntity.getUserId())
                .userFirstname(messageEntity.getUserFirstname())
                .userNick(messageEntity.getUserNick())
                .nationality(messageEntity.getNationality())
                .message(messageEntity.getMessage())
                .isDocument(messageEntity.getIsDocument())
                .isPhoto(messageEntity.getIsPhoto())
                .isAudio(messageEntity.getIsAudio())
                .isAnimation(messageEntity.getIsAnimation())
                .isVideo(messageEntity.getIsVideo())
                .fileId(messageEntity.getFileId())
                .messageDate(messageEntity.getMessageDate())
                .build();
    }

    public List<MessageEntityResponse> map(List<MessageEntity> messageEntityList) {
        return messageEntityList.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
