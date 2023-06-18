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
public class PassportElementErrorFiles implements PassportElementError {
    /**
     * Represents an issue with a list of scans.
     * The error is considered resolved when the list of files containing the scans changes.
     *
     * @param source     Error source, must be files.
     * @param type       The section of the user's Telegram Passport which has the issue, one of "utility_bill", "bank_statement",
     *                   "rental_agreement", "passport_registration", "temporary_registration".
     * @param fileHashes List of base64-encoded file hashes.
     * @param message    Error message.
     */
    @JsonProperty("source")
    private final String source = "files";

    @JsonProperty("type")
    private String type;

    @JsonProperty("file_hashes")
    private List<String> fileHashes;

    @JsonProperty("message")
    private String message;
}
