package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BotDescription implements ApiObject {
    @JsonProperty("description")
    private String description;

    private BotDescription() {
    }
}
