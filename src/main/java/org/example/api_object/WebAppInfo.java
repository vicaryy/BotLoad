package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class WebAppInfo implements ApiObject {
    @JsonProperty("url")
    private String url;

    private WebAppInfo() {
    }
}
