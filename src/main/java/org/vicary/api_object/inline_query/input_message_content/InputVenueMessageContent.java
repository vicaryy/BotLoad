package org.vicary.api_object.inline_query.input_message_content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class InputVenueMessageContent implements InputMessageContent {
    /**
     * Represents the content of a venue message to be sent as the result of an inline query.
     *
     * @param latitude         Latitude of the venue in degrees
     * @param longitude        Longitude of the venue in degrees
     * @param title            Name of the venue
     * @param address          Address of the venue
     * @param foursquareId     Optional. Foursquare identifier of the venue, if known
     * @param foursquareType   Optional. Foursquare type of the venue, if known. (For example, "arts_entertainment/default", "arts_entertainment/aquarium" or "food/icecream".)
     * @param googlePlaceId    Optional. Google Places identifier of the venue
     * @param googlePlaceType  Optional. Google Places type of the venue. (See supported types.)
     */
    @JsonProperty("latitude")
    private Float latitude;

    @JsonProperty("longitude")
    private Float longitude;

    @JsonProperty("title")
    private String title;

    @JsonProperty("address")
    private String address;

    @JsonProperty("foursquare_id")
    private String foursquareId;

    @JsonProperty("foursquare_type")
    private String foursquareType;

    @JsonProperty("google_place_id")
    private String googlePlaceId;

    @JsonProperty("google_place_type")
    private String googlePlaceType;

    private InputVenueMessageContent() {
    }
}
