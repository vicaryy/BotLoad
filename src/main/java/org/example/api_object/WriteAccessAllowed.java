package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class WriteAccessAllowed implements ApiObject {
    @JsonProperty("web_app_name")
    private String webAppName;

    private WriteAccessAllowed() {
    }
}
