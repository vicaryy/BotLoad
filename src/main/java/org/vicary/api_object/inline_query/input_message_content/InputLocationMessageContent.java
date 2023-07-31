package org.vicary.api_object.inline_query.input_message_content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class InputLocationMessageContent implements InputMessageContent {
    /**
     * Represents the content of a location message to be sent as the result of an inline query.
     *
     * @param latitude                Latitude of the location in degrees
     * @param longitude               Longitude of the location in degrees
     * @param horizontalAccuracy      Optional. The radius of uncertainty for the location, measured in meters; 0-1500
     * @param livePeriod              Optional. Period in seconds for which the location can be updated, should be between 60 and 86400.
     * @param heading                 Optional. For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
     * @param proximityAlertRadius    Optional. For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
     */
    @JsonProperty("latitude")
    private Float latitude;

    @JsonProperty("longitude")
    private Float longitude;

    @JsonProperty("horizontal_accuracy")
    private Float horizontalAccuracy;

    @JsonProperty("live_period")
    private Integer livePeriod;

    @JsonProperty("heading")
    private Integer heading;

    @JsonProperty("proximity_alert_radius")
    private Integer proximityAlertRadius;

    private InputLocationMessageContent() {
    }
}
