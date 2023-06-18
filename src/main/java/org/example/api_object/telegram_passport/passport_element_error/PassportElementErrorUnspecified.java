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
public class PassportElementErrorUnspecified implements PassportElementError {
    /**
     * Represents an issue in an unspecified place.
     * The error is considered resolved when new data is added.
     *
     * @param source       Error source, must be unspecified.
     * @param type         Type of element of the user's Telegram Passport which has the issue.
     * @param elementHash  Base64-encoded element hash.
     * @param message      Error message.
     */
    @JsonProperty("source")
    private final String source = "unspecified";

    @JsonProperty("type")
    private String type;

    @JsonProperty("element_hash")
    private String elementHash;

    @JsonProperty("message")
    private String message;
}
