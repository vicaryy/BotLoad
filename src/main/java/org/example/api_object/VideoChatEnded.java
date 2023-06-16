package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class VideoChatEnded implements ApiObject {
    @JsonProperty("duration")
    private Integer duration;

    private VideoChatEnded() {
    }
}
