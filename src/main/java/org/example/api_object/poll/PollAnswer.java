package org.example.api_object.poll;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;
import org.example.api_object.User;

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
