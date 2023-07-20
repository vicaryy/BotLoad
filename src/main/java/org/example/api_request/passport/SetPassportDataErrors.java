package org.example.api_request.passport;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.telegram_passport.passport_element_error.PassportElementError;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

import java.util.List;

@Data
@RequiredArgsConstructor
@Builder
public class SetPassportDataErrors implements ApiRequest<Boolean> {
    /**
     * Informs a user that some of the Telegram Passport elements they provided contains errors.
     * The user will not be able to re-submit their Passport to you until the errors are fixed
     * (the contents of the field for which you returned the error must change). Returns True on success.
     * Use this if the data submitted by the user doesn't satisfy the standards your service requires for any reason.
     * For example, if a birthday date seems invalid, a submitted document is blurry, a scan shows evidence of tampering,
     * etc. Supply some details in the error message to make sure the user knows how to correct the issues.
     *
     * @param userId    User identifier.
     * @param errors    A JSON-serialized array describing the errors.
     */

    @NonNull
    @JsonProperty("user_id")
    private Integer userId;

    @NonNull
    @JsonProperty("errors")
    private List<PassportElementError> errors;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SET_PASSPORT_DATA_ERRORS.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
