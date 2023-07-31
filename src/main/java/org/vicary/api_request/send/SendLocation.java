package org.vicary.api_request.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.message.Message;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SendLocation implements ApiRequest<Message> {
    /**
     * Use this method to send a point on the map. On success, the sent Message is returned.
     *
     * @param chatId                Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId       Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param latitude              Latitude of the location.
     * @param longitude             Longitude of the location.
     * @param horizontalAccuracy    Optional. The radius of uncertainty for the location, measured in meters; 0-1500.
     * @param livePeriod            Optional. Period in seconds for which the location will be updated (see Live Locations), should be between 60 and 86400.
     * @param heading               Optional. For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
     * @param proximityAlertRadius  Optional. For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
     * @param disableNotification   Optional. Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent        Optional. Protects the contents of the sent message from forwarding and saving.
     * @param replyToMessageId      Optional. If the message is a reply, ID of the original message.
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

    @JsonProperty("horizontal_accuracy")
    private Float horizontalAccuracy;

    @JsonProperty("live_period")
    private Integer livePeriod;

    @JsonProperty("heading")
    private Integer heading;

    @JsonProperty("proximity_alert_radius")
    private Integer proximityAlertRadius;

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @JsonProperty("protect_content")
    private Boolean protectContent;

    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;

//    @JsonProperty("reply_markup")
//    private ReplyMarkup replyMarkup;

    @Override
    public Message getReturnObject() {
        return new Message();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SEND_LOCATION.getPath();
    }

    @Override
    public void checkValidation() {

    }
}
