package org.vicary.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class WebAppData implements ApiObject {
    @JsonProperty("data")
    private String data;

    @JsonProperty("button_text")
    private String buttonText;

    private WebAppData() {
    }
}
