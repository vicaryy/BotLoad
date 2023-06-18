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
public class PassportElementErrorFile implements PassportElementError {
    /**
     * Represents an issue with a document scan.
     * The error is considered resolved when the file with the document scan changes.
     *
     * @param source    Error source, must be file.
     * @param type      The section of the user's Telegram Passport which has the issue, one of “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”, “temporary_registration”.
     * @param fileHash  Base64-encoded file hash.
     * @param message   Error message.
     */
    @JsonProperty("source")
    private final String source = "file";

    @JsonProperty("type")
    private String type;

    @JsonProperty("file_hash")
    private String fileHash;

    @JsonProperty("message")
    private String message;
}
