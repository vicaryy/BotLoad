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
        String userNick = update.getMessage().getFrom().getUsername();
        String nationality = update.getMessage().getFrom().getLanguageCode();
        String text = update.getMessage().getText();
        String fileId = getFileId(update);

        return MessageEntity.builder()
                .chatId(chatId)
                .userId(userId)
                .userNick(userNick)
                .nationality(nationality)
                .message(text)
                .fileId(fileId)
                .messageDate(dataTime.now())
                .build();
    }

    private static String getFileId(Update update) {
        String fileId = null;

        if (update.getMessage().getDocument() != null) {
            fileId = update.getMessage().getDocument().getFileId();
        } else if (update.getMessage().getAudio() != null) {
            fileId = update.getMessage().getAudio().getFileId();
        } else if (update.getMessage().getAnimation() != null) {
            fileId = update.getMessage().getAnimation().getFileId();
        } else if (update.getMessage().getVideo() != null) {
            fileId = update.getMessage().getVideo().getFileId();
        } else if (update.getMessage().getSticker() != null) {
            fileId = update.getMessage().getSticker().getFileId();
        } else if (update.getMessage().getVoice() != null) {
            fileId = update.getMessage().getVoice().getFileId();
        }
        return fileId;
    }

    public MessageEntityResponse map(MessageEntity messageEntity) {
        return MessageEntityResponse.builder()
                .messageId(messageEntity.getMessageId())
                .chatId(messageEntity.getChatId())
                .userId(messageEntity.getUserId())
                .userNick(messageEntity.getUserNick())
                .nationality(messageEntity.getNationality())
                .message(messageEntity.getMessage())
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
