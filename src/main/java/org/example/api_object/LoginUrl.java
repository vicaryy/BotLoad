package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LoginUrl implements ApiObject {
    @JsonProperty("url")
    private String url;

    @JsonProperty("forward_text")
    private String forwardText;

    @JsonProperty("bot_username")
    private String botUsername;

    @JsonProperty("request_write_access")
    private Boolean requestWriteAccess;

    private LoginUrl() {
    }
}
