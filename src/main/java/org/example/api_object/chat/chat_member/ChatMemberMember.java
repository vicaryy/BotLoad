package org.example.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.User;

@Getter
public class ChatMemberMember implements ChatMember {
    @JsonProperty("status")
    private final String status = "member";

    @JsonProperty("user")
    private User user;

    private ChatMemberMember() {
    }
}
