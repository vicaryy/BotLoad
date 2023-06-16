package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BotName implements ApiObject {
    @JsonProperty("name")
    private String name;

    private BotName() {
    }
}
