package org.example.api_object.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class BotShortDescription implements ApiObject {
    @JsonProperty("short_description")
    private String shortDescription;

    private BotShortDescription() {
    }
}
