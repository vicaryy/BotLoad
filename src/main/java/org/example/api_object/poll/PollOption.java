package org.example.api_object.poll;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class PollOption implements ApiObject {
    @JsonProperty("text")
    private String text;

    @JsonProperty("voter_count")
    private Integer voterCount;

    private PollOption() {
    }
}
