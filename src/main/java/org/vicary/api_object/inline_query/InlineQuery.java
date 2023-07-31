package org.vicary.api_object.inline_query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.ApiObject;
import org.vicary.api_object.Location;
import org.vicary.api_object.User;

@Getter
@ToString
@EqualsAndHashCode
public class InlineQuery implements ApiObject {
    /**
     * This object represents an incoming inline query.
     * When the user sends an empty query, your bot could return some default or trending results.
     */
    @JsonProperty("id")
    public String id;

    @JsonProperty("from")
    public User from;

    @JsonProperty("query")
    public String query;

    @JsonProperty("offset")
    public String offset;

    @JsonProperty("chat_type")
    public String chatType;

    @JsonProperty("location")
    public Location location;

    private InlineQuery() {
    }
}
