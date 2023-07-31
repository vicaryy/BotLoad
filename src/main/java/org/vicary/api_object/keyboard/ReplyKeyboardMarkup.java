package org.vicary.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.ApiObject;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyKeyboardMarkup implements ApiObject, ReplyMarkup {
    @JsonProperty("keyboard")
    private List<List<KeyboardButton>> keyboard;

    @JsonProperty("is_persistent")
    private Boolean isPersistent;

    @JsonProperty("resize_keyboard")
    private Boolean resizeKeyboard;

    @JsonProperty("one_time_keyboard")
    private Boolean oneTimeKeyboard;

    @JsonProperty("input_field_placeholder")
    private String inputFieldPlaceholder;

    @JsonProperty("selective")
    private Boolean selective;
}
