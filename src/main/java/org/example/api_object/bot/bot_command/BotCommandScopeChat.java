package org.example.api_object.bot.bot_command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotCommandScopeChat implements BotCommandScope {
    @JsonProperty("type")
    private final String type = "chat";

    @JsonProperty("chat_id")
    private String chatId;
}