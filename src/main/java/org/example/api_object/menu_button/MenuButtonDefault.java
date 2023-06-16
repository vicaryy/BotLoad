package org.example.api_object.menu_button;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MenuButtonDefault implements MenuButton {
    @JsonProperty("type")
    private final String type = "default";

    private MenuButtonDefault() {
    }
}
