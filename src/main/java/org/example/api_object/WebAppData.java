package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class WebAppData implements ApiObject {
    @JsonProperty("data")
    private String data;

    @JsonProperty("button_text")
    private String buttonText;

    private WebAppData() {
    }
}
