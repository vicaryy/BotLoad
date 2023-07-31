package org.vicary.api_object.menu_button;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class MenuButtonCommands implements MenuButton {
    @JsonProperty("type")
    private final String type = "commands";

    private MenuButtonCommands() {
    }
}
