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

    @Column(name = "USER_NICK")
    private String userNick;

    @Column(name = "NATIONALITY")
    private String nationality;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "FILE_ID")
    private String fileId;

    @Column(name = "MESSAGE_DATE")
    private OffsetDateTime messageDate;
}
