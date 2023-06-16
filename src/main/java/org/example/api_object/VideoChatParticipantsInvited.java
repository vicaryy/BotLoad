package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class VideoChatParticipantsInvited implements ApiObject {
    @JsonProperty("users")
    private List<User> users;

    private VideoChatParticipantsInvited() {
    }
}
