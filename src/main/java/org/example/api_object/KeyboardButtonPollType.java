package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KeyboardButtonPollType implements ApiObject {
    @JsonProperty("type")
    private String type;

    private KeyboardButtonPollType() {
    }
}
