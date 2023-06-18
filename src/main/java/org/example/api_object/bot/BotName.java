package org.example.api_object.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class BotName implements ApiObject {
    @JsonProperty("name")
    private String name;

    private BotName() {
    }
}
