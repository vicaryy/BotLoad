package org.vicary.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.inline_query.input_message_content.InputMessageContent;
import org.vicary.api_object.keyboard.InlineKeyboardMarkup;

@Getter
@ToString
@EqualsAndHashCode
public class InlineQueryResultLocation implements InlineQueryResult {
    /**
     * Represents a location on a map. By default, the location will be sent by the user.
     * Alternatively, you can use input_message_content to send a message with the specified content instead of the location.
     */
    @JsonProperty("type")
    private final String type = "location";

    @JsonProperty("id")
    private String id;

    @JsonProperty("latitude")
    private float latitude;

    @JsonProperty("longitude")
    private float longitude;

    @JsonProperty("title")
    private String title;

    @JsonProperty("horizontal_accuracy")
    private Float horizontalAccuracy;

    @JsonProperty("live_period")
    private Integer livePeriod;

    @JsonProperty("heading")
    private Integer heading;

    @JsonProperty("proximity_alert_radius")
    private Integer proximityAlertRadius;

    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    @JsonProperty("input_message_content")
    private InputMessageContent inputMessageContent;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("thumbnail_width")
    private Integer thumbnailWidth;

    @JsonProperty("thumbnail_height")
    private Integer thumbnailHeight;

    private InlineQueryResultLocation() {
    }
}
