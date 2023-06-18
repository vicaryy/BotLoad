package org.example.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class WriteAccessAllowed implements ApiObject {
    @JsonProperty("web_app_name")
    private String webAppName;

    private WriteAccessAllowed() {
    }
}
