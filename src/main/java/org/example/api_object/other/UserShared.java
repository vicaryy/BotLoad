package org.example.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class UserShared implements ApiObject {
    @JsonProperty("request_id")
    private Integer requestId;

    @JsonProperty("user_id")
    private Integer userId;

    private UserShared() {
    }
}
