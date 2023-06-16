package org.example.api_object.bot_command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BotCommandScopeAllPrivateChats implements BotCommandScope {
    @JsonProperty("type")
    private final String type = "all_private_chats";

    private BotCommandScopeAllPrivateChats() {
    }
}
