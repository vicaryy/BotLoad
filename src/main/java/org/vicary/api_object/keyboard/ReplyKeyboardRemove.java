package org.vicary.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.ApiObject;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyKeyboardRemove implements ApiObject, ReplyMarkup {
    @JsonProperty("remove_keyboard")
    private Boolean removeKeyboard;

    @JsonProperty("selective")
    private Boolean selective;
}
