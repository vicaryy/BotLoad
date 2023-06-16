package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

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
