package org.example.api_object.telegram_passport.passport_element_error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassportElementErrorTranslationFiles implements PassportElementError {
    /**
     * Represents an issue with the translated version of a document.
     * The error is considered resolved when a file with the document translation changes.
     *
     * @param source      Error source, must be translation_files.
     * @param type        Type of element of the user's Telegram Passport which has the issue,
     *                    one of “passport”, “driver_license”, “identity_card”, “internal_passport”,
     *                    “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”, “temporary_registration”.
     * @param fileHashes  List of base64-encoded file hashes.
     * @param message     Error message.
     */
    @JsonProperty("source")
    private final String source = "translation_files";

    @JsonProperty("type")
    private String type;

    @JsonProperty("file_hashes")
    private List<String> fileHashes;

    @JsonProperty("message")
    private String message;
}
