package org.example.api_object.bot.bot_command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class BotCommand implements ApiObject {
    @JsonProperty("command")
    private String command;

    @JsonProperty("description")
    private String description;

    private BotCommand() {
    }
}
