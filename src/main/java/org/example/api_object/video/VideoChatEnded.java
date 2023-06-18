package org.example.api_object.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class VideoChatEnded implements ApiObject {
    @JsonProperty("duration")
    private Integer duration;

    private VideoChatEnded() {
    }
}
