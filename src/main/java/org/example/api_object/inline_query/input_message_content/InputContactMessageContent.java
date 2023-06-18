package org.example.api_object.inline_query.input_message_content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class InputContactMessageContent implements InputMessageContent {
    /**
     * Represents the content of a contact message to be sent as the result of an inline query.
     *
     * @param phoneNumber   Contact's phone number
     * @param firstName     Contact's first name
     * @param lastName      Optional. Contact's last name
     * @param vcard         Optional. Additional data about the contact in the form of a vCard, 0-2048 bytes
     */
    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("vcard")
    private String vcard;

    private InputContactMessageContent() {
    }
}
