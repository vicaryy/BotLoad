package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserShared implements ApiObject {
    @JsonProperty("request_id")
    private Integer requestId;

    @JsonProperty("user_id")
    private Integer userId;

    private UserShared() {
    }
}
