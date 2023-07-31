package org.vicary.api_object.bot.bot_command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
public class BotCommandScopeDefault implements BotCommandScope {
    @JsonProperty("type")
    private final String type = "default";
}
