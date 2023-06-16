package org.example.api_object.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;
import org.example.api_object.Location;

@Getter
public class ChatLocation implements ApiObject {
    @JsonProperty("location")
    private Location location;

    @JsonProperty("address")
    private String address;

    private ChatLocation() {
    }
}
