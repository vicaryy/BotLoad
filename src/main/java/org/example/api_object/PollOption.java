package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PollOption implements ApiObject {
    @JsonProperty("text")
    private String text;

    @JsonProperty("voter_count")
    private Integer voterCount;

    private PollOption() {
    }
}
