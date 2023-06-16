package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class VideoChatScheduled implements ApiObject {
    @JsonProperty("start_date")
    private Integer startDate;

    private VideoChatScheduled() {
    }
}
