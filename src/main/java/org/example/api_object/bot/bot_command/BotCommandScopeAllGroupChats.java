package org.example.api_object.bot.bot_command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class BotCommandScopeAllGroupChats implements BotCommandScope {
    @JsonProperty("type")
    private final String type = "all_group_chats";

    private BotCommandScopeAllGroupChats() {
    }
}
