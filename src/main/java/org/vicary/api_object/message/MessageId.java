package org.vicary.api_object.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.ApiObject;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageId implements ApiObject {
    @JsonProperty("message_id")
    private Integer messageId;
}
