package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BotShortDescription implements ApiObject {
    @JsonProperty("short_description")
    private String shortDescription;

    private BotShortDescription() {
    }
}
