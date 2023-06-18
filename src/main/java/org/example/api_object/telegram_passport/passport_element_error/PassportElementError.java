package org.example.api_object.telegram_passport.passport_element_error;

import org.example.api_object.ApiObject;

public interface PassportElementError extends ApiObject {
    String getSource();

    String getType();

    String getMessage();
}
