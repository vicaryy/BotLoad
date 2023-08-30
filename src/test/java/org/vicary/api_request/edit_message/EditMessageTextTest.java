package org.vicary.api_request.edit_message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EditMessageTextTest {
    @Test
    void checkValidation_expectThrowIllegalArgumentEx_WrongParseMode() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("chatId")
                .messageId(123)
                .text("text")
                .parseMode("randomParseMode")
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, editMessageText::checkValidation);
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_ProperParseModeButEntitiesIsSpecify() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("chatId")
                .messageId(123)
                .text("text")
                .parseMode("randomParseMode")
                .entities(new ArrayList<>())
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, editMessageText::checkValidation);
    }
}