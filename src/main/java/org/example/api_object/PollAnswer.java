package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class PollAnswer implements ApiObject {
    @JsonProperty("poll_id")
    private String pollId;

    @JsonProperty("user")
    private User user;

    @JsonProperty("option_ids")
    private List<Integer> optionIds;

    private PollAnswer() {
    }
}
