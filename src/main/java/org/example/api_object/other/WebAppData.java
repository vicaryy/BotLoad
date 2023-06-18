package org.example.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class WebAppData implements ApiObject {
    @JsonProperty("data")
    private String data;

    @JsonProperty("button_text")
    private String buttonText;

    private WebAppData() {
    }
}
