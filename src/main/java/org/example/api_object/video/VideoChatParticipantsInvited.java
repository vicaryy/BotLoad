package org.example.api_object.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;
import org.example.api_object.User;

import java.util.List;

@Getter
public class VideoChatParticipantsInvited implements ApiObject {
    @JsonProperty("users")
    private List<User> users;

    private VideoChatParticipantsInvited() {
    }
}
