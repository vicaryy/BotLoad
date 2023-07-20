package org.example.api_object.bot.bot_command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.ApiObject;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BotCommand implements ApiObject {
    @JsonProperty("command")
    private String command;

    @JsonProperty("description")
    private String description;
}
