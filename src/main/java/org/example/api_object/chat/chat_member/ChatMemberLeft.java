package org.example.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.User;

@Getter
@ToString
@EqualsAndHashCode
public class ChatMemberLeft implements ChatMember{
    @JsonProperty("status")
    private final String status = "left";

    @JsonProperty("user")
    private User user;

}
