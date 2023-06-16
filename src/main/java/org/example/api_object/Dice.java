package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Dice implements ApiObject {
    @JsonProperty("emoji")
    private String emoji;

    @JsonProperty("value")
    private Integer value;

    private Dice() {
    }
}
