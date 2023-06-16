package org.example.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.User;

@Getter
public class ChatMemberBanned implements ChatMember{
    @JsonProperty("status")
    private final String status = "kicked";

    @JsonProperty("user")
    private User user;

    @JsonProperty("until_date")
    private Integer untilDate;

}
