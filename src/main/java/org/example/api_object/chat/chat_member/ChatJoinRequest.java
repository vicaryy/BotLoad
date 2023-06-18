package org.example.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.ApiObject;
import org.example.api_object.chat.Chat;
import org.example.api_object.User;
import org.example.api_object.chat.ChatInviteLink;

@Getter
@ToString
@EqualsAndHashCode
public class ChatJoinRequest implements ApiObject {
    @JsonProperty("chat")
    private Chat chat;

    @JsonProperty("from")
    private User from;

    @JsonProperty("user_chat_id")
    private Long userChatId;

    @JsonProperty("date")
    private Integer date;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("invite_link")
    private ChatInviteLink inviteLink;

    private ChatJoinRequest() {
    }
}
