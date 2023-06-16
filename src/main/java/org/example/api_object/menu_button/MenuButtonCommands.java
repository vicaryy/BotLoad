package org.example.api_object.menu_button;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MenuButtonCommands implements MenuButton {
    @JsonProperty("type")
    private final String type = "commands";

    private MenuButtonCommands() {
    }
}
