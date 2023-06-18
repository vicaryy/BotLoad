package org.example.api_object.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class VideoChatScheduled implements ApiObject {
    @JsonProperty("start_date")
    private Integer startDate;

    private VideoChatScheduled() {
    }
}
