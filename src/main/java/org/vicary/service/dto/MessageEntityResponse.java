package org.vicary.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageEntityResponse {
    private Long messageId;

    private String chatId;

    private String userId;

    private String userFirstname;

    private String userNick;

    private String nationality;

    private String message;

    private Boolean isDocument;

    private Boolean isPhoto;

    private Boolean isAudio;

    private Boolean isAnimation;

    private Boolean isVideo;

    private String fileId;

    private OffsetDateTime messageDate;
}
