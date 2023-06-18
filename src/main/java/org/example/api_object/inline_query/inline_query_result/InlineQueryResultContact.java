package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.keyboard.InlineKeyboardMarkup;

@Getter
public class InlineQueryResultContact implements InlineQueryResult {
    /**
     * Represents a contact with a phone number. By default, this contact will be sent by the user.
     * Alternatively, you can use input_message_content to send a message with the specified content instead of the contact.
     */
    @JsonProperty("type")
    private final String type = "contact";

    @JsonProperty("id")
    public String id;

    @JsonProperty("phone_number")
    public String phoneNumber;

    @JsonProperty("first_name")
    public String firstName;

    @JsonProperty("last_name")
    public String lastName;

    @JsonProperty("vcard")
    public String vcard;

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

    private InlineQueryResultContact() {
    }
}
