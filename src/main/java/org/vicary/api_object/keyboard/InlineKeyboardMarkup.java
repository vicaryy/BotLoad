package org.vicary.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.ApiObject;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class InlineKeyboardMarkup implements ApiObject, ReplyMarkup {
    @JsonProperty("inline_keyboard")
    private List<List<InlineKeyboardButton>> inlineKeyboard;
}
