package org.example.api_object.bot_command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BotCommandScopeAllGroupChats implements BotCommandScope {
    @JsonProperty("type")
    private final String type = "all_group_chats";

    private BotCommandScopeAllGroupChats() {
    }
}
