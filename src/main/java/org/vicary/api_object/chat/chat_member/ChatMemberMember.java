package org.vicary.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.User;

@Getter
@ToString
@EqualsAndHashCode
public class ChatMemberMember implements ChatMember {
    @JsonProperty("status")
    private final String status = "member";

    @JsonProperty("user")
    private User user;

    private ChatMemberMember() {
    }
}
