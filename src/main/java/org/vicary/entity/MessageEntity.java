package org.vicary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "MESSAGES")
public class MessageEntity {
    @Id
    @Column(name = "MESSAGE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(name = "CHAT_ID")
    private String chatId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_FIRSTNAME")
    private String userFirstname;

    @Column(name = "USER_NICK")
    private String userNick;

    @Column(name = "NATIONALITY")
    private String nationality;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "IS_DOCUMENT")
    private Boolean isDocument;

    @Column(name = "IS_PHOTO")
    private Boolean isPhoto;

    @Column(name = "IS_AUDIO")
    private Boolean isAudio;

    @Column(name = "IS_ANIMATION")
    private Boolean isAnimation;

    @Column(name = "IS_VIDEO")
    private Boolean isVideo;

    @Column(name = "FILE_ID")
    private String fileId;

    @Column(name = "MESSAGE_DATE")
    private OffsetDateTime messageDate;
}
