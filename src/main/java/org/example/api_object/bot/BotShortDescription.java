package org.example.api_object.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class BotShortDescription implements ApiObject {
    @JsonProperty("short_description")
    private String shortDescription;

    private BotShortDescription() {
    }
}
