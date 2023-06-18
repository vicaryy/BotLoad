package org.example.api_object.bot.bot_command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BotCommandScopeAllChatAdministrators implements BotCommandScope {
    @JsonProperty("type")
    private final String type = "all_chat_administrators";

    private BotCommandScopeAllChatAdministrators() {
    }
}
