package org.example.api_object.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.ApiObject;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotShortDescription implements ApiObject {
    @JsonProperty("short_description")
    private String shortDescription;
}
