package org.vicary.api_object.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.vicary.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class BotDescription implements ApiObject {
    @JsonProperty("description")
    private String description;
}
