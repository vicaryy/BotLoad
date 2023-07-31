package org.vicary.api_object.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class SwitchInlineQueryChosenChat implements ApiObject {
    @JsonProperty("query")
    private String query;

    @JsonProperty("allow_user_chats")
    private Boolean allowUserChats;

    @JsonProperty("allow_bot_chats")
    private Boolean allowBotChats;

    @JsonProperty("allow_group_chats")
    private Boolean allowGroupChats;

    @JsonProperty("allow_channel_chats")
    private Boolean allowChannelChats;

    private SwitchInlineQueryChosenChat() {
    }
}
