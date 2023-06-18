package org.example.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class WebAppInfo implements ApiObject {
    @JsonProperty("url")
    private String url;

    private WebAppInfo() {
    }
}
