package org.example.api_object.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
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
