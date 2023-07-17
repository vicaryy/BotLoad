package org.example.api_request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.message.Message;
import org.example.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SendVenue implements ApiRequest<Message> {
    /**
     * Use this method to send information about a venue. On success, the sent Message is returned.
     *
     * @param chatId              Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId     Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param latitude            Latitude of the venue.
     * @param longitude           Longitude of the venue.
     * @param title               Name of the venue.
     * @param address             Address of the venue.
     * @param foursquareId        Optional. Foursquare identifier of the venue.
     * @param foursquareType      Optional. Foursquare type of the venue, if known. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
     * @param googlePlaceId       Optional. Google Places identifier of the venue.
     * @param googlePlaceType     Optional. Google Places type of the venue. (See supported types.)
     * @param disableNotification Optional. Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent      Optional. Protects the contents of the sent message from forwarding and saving.
     * @param replyToMessageId    Optional. If the message is a reply, ID of the original message.
     * @param allowSendingWithoutReply Optional. Pass True if the message should be sent even if the specified replied-to message is not found.
     */

    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @NonNull
    @JsonProperty("latitude")
    private Float latitude;

    @NonNull
    @JsonProperty("longitude")
    private Float longitude;

    @NonNull
    @JsonProperty("title")
    private String title;

    @NonNull
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

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @JsonProperty("protect_content")
    private Boolean protectContent;

    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;

    @Override
    public Message getReturnObject() {
        return new Message();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SEND_VENUE.getPath();
    }

    @Override
    public void checkValidation() {

    }
}
