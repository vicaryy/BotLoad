package org.vicary.api_object.bot.bot_command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
public class BotCommandScopeChatAdministrators implements BotCommandScope {
    @JsonProperty("type")
    private final String type = "chat_administrators";

    @JsonProperty("chat_id")
    private String chatId;
}
