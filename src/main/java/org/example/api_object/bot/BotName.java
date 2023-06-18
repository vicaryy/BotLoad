package org.example.api_object.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class BotName implements ApiObject {
    @JsonProperty("name")
    private String name;

    private BotName() {
    }
}
