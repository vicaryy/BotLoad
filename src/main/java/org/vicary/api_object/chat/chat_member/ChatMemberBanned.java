package org.vicary.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.User;

@Getter
@ToString
@EqualsAndHashCode
public class ChatMemberBanned implements ChatMember{
    @JsonProperty("status")
    private final String status = "kicked";

    @JsonProperty("user")
    private User user;

    @JsonProperty("until_date")
    private Integer untilDate;

}
