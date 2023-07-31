package org.vicary.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.ApiObject;
import org.vicary.api_object.keyboard.ReplyMarkup;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ForceReply implements ApiObject, ReplyMarkup {
    @JsonProperty("force_reply")
    private Boolean forceReply;

    @JsonProperty("input_field_placeholder")
    private String inputFieldPlaceholder;

    @JsonProperty("selective")
    private Boolean selective;
}
