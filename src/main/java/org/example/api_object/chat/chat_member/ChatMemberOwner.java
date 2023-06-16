package org.example.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.User;

@Getter
public class ChatMemberOwner implements ChatMember {
    @JsonProperty("status")
    private final String status = "creator";

    @JsonProperty("user")
    private User user;

    @JsonProperty("is_anonymous")
    private Boolean isAnonymous;

    @JsonProperty("custom_title")
    private String customTitle;

    private ChatMemberOwner() {
    }
}
