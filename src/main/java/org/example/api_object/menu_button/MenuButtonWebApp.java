package org.example.api_object.menu_button;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.other.WebAppInfo;

@Getter
public class MenuButtonWebApp implements MenuButton {
    @JsonProperty("type")
    private final String type = "web_app";

    @JsonProperty("text")
    private String text;

    @JsonProperty("web_app")
    private WebAppInfo webAppInfo;

    private MenuButtonWebApp() {
    }
}
