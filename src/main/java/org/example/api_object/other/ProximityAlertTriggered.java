package org.example.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;
import org.example.api_object.User;

@Getter
public class ProximityAlertTriggered implements ApiObject {
    @JsonProperty("traveler")
    private User traveler;

    @JsonProperty("watcher")
    private User watcher;

    @JsonProperty("distance")
    private Integer distance;

    private ProximityAlertTriggered() {
    }
}
