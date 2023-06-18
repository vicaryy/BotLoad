package org.example.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class ForceReply implements ApiObject {
    @JsonProperty("force_reply")
    private Boolean forceReply;

    @JsonProperty("input_field_placeholder")
    private String inputFieldPlaceholder;

    @JsonProperty("selective")
    private Boolean selective;

    private ForceReply() {
    }
}
