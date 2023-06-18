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
public class PassportElementErrorFrontSide implements PassportElementError {
    /**
     * Represents an issue with the front side of a document.
     * The error is considered resolved when the file with the front side of the document changes.
     *
     * @param source    Error source, must be front_side.
     * @param type      The section of the user's Telegram Passport which has the issue, one of “passport”, “driver_license”, “identity_card”, “internal_passport”.
     * @param fileHash  Base64-encoded hash of the file with the front side of the document.
     * @param message   Error message.
     */
    @JsonProperty("source")
    private final String source = "front_side";

    @JsonProperty("type")
    private String type;

    @JsonProperty("file_hash")
    private String fileHash;

    @JsonProperty("message")
    private String message;
}
