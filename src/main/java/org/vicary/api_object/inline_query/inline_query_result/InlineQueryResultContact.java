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
public class InlineQueryResultContact implements InlineQueryResult {
    /**
     * Represents a contact with a phone number. By default, this contact will be sent by the user.
     * Alternatively, you can use input_message_content to send a message with the specified content instead of the contact.
     */
    @JsonProperty("type")
    private final String type = "contact";

    @JsonProperty("id")
    private String id;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("vcard")
    private String vcard;

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

    private InlineQueryResultContact() {
    }
}
