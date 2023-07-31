package org.vicary.api_object.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.ApiObject;

@Data
@NoArgsConstructor
public class BotName implements ApiObject {
    @JsonProperty("name")
    private String name;
}
