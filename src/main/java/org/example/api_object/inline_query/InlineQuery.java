package org.example.api_object.inline_query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;
import org.example.api_object.Location;
import org.example.api_object.User;

@Getter
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
