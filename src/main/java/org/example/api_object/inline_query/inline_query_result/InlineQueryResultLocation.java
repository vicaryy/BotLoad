package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.keyboard.InlineKeyboardMarkup;

@Getter
public class InlineQueryResultLocation implements InlineQueryResult {
    /**
     * Represents a location on a map. By default, the location will be sent by the user.
     * Alternatively, you can use input_message_content to send a message with the specified content instead of the location.
     */
    @JsonProperty("type")
    private final String type = "location";

    @JsonProperty("id")
    public String id;

    @JsonProperty("latitude")
    public float latitude;

    @JsonProperty("longitude")
    public float longitude;

    @JsonProperty("title")
    public String title;

    @JsonProperty("horizontal_accuracy")
    public Float horizontalAccuracy;

    @JsonProperty("live_period")
    public Integer livePeriod;

    @JsonProperty("heading")
    public Integer heading;

    @JsonProperty("proximity_alert_radius")
    public Integer proximityAlertRadius;

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

    private InlineQueryResultLocation() {
    }
}
