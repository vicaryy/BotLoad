package org.vicary.api_object.menu_button;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.other.WebAppInfo;

@Getter
@ToString
@EqualsAndHashCode
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
