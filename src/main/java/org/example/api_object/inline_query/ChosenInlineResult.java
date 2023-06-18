package org.example.api_object.inline_query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.ApiObject;
import org.example.api_object.Location;
import org.example.api_object.User;

@Getter
@ToString
@EqualsAndHashCode
public class ChosenInlineResult implements ApiObject {
    /**
     * Represents a result of an inline query that was chosen by the user and sent to their chat partner.
     *
     * @param resultId          The unique identifier for the result that was chosen
     * @param from              The user that chose the result
     * @param location          Optional. Sender location, only for bots that require user location
     * @param inlineMessageId   Optional. Identifier of the sent inline message. Available only if there is an inline keyboard attached to the message. Will be also received in callback queries and can be used to edit the message.
     * @param query             The query that was used to obtain the result
     */
    @JsonProperty("result_id")
    private String resultId;

    @JsonProperty("from")
    private User from;

    @JsonProperty("location")
    private Location location;

    @JsonProperty("inline_message_id")
    private String inlineMessageId;

    @JsonProperty("query")
    private String query;
}
