package org.vicary.api_object.telegram_passport.passport_element_error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassportElementErrorTranslationFile implements PassportElementError {
    /**
     * Represents an issue with one of the files that constitute the translation of a document.
     * The error is considered resolved when the file changes.
     *
     * @param source    Error source, must be translation_file.
     * @param type      Type of element of the user's Telegram Passport which has the issue,
     *                  one of “passport”, “driver_license”, “identity_card”, “internal_passport”,
     *                  “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”, “temporary_registration”.
     * @param fileHash  Base64-encoded file hash.
     * @param message   Error message.
     */
    @JsonProperty("source")
    private final String source = "translation_file";

    @JsonProperty("type")
    private String type;

    @JsonProperty("file_hash")
    private String fileHash;

    @JsonProperty("message")
    private String message;
}
