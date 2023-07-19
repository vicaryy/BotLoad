package org.example.api_object.inline_query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.api_object.ApiObject;

@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class SentWebAppMessage implements ApiObject {
    /**
     * Describes an inline message sent by a Web App on behalf of a user.
     *
     * @param inline_message_id  Optional. Identifier of the sent inline message. Available only if there is an inline keyboard attached to the message.
     */

    @JsonProperty("inline_message_id")
    private String inlineMessageId;
}
