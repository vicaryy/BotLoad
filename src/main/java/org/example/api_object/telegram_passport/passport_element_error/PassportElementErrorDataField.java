package org.example.api_object.telegram_passport.passport_element_error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassportElementErrorDataField implements PassportElementError {
    /**
     * Represents an issue in one of the data fields that was provided by the user.
     * The error is considered resolved when the field's value changes.
     *
     * @param source     Error source, must be data.
     * @param type       The section of the user's Telegram Passport which has the error, one of “personal_details”, “passport”, “driver_license”, “identity_card”, “internal_passport”, “address”.
     * @param fieldName  Name of the data field which has the error.
     * @param dataHash   Base64-encoded data hash.
     * @param message    Error message.
     */
    @JsonProperty("source")
    private final String source = "data";

    @JsonProperty("type")
    private String type;

    @JsonProperty("field_name")
    private String fieldName;

    @JsonProperty("data_hash")
    private String dataHash;

    @JsonProperty("message")
    private String message;
}
