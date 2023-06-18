package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.keyboard.InlineKeyboardMarkup;

@Getter
public class InlineQueryResultVenue implements InlineQueryResult {
    /**
     * Represents a venue. By default, the venue will be sent by the user.
     * Alternatively, you can use input_message_content to send a message with the specified content instead of the venue.
     */
    @JsonProperty("type")
    private final String type = "venue";

    @JsonProperty("id")
    public String id;

    @JsonProperty("latitude")
    public Float latitude;

    @JsonProperty("longitude")
    public Float longitude;

    @JsonProperty("title")
    public String title;

    @JsonProperty("address")
    public String address;

    @JsonProperty("foursquare_id")
    public String foursquareId;

    @JsonProperty("foursquare_type")
    public String foursquareType;

    @JsonProperty("google_place_id")
    public String googlePlaceId;

    @JsonProperty("google_place_type")
    public String googlePlaceType;

    @JsonProperty("reply_markup")
    public InlineKeyboardMarkup replyMarkup;

    @JsonProperty("input_message_content")
    public InputMessageContent inputMessageContent;

    @JsonProperty("thumbnail_url")
    public String thumbnailUrl;

    @JsonProperty("thumbnail_width")
    public Integer thumbnailWidth;

    @JsonProperty("thumbnail_height")
    public Integer thumbnailHeight;

    private InlineQueryResultVenue() {
    }
}
