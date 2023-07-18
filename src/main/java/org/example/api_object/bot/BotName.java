package org.example.api_object.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.ApiObject;

@Data
@NoArgsConstructor
public class BotName implements ApiObject {
    @JsonProperty("name")
    private String name;
}
