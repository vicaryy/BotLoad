package org.vicary.api_request.send;

import org.junit.jupiter.api.Test;
import org.vicary.api_object.message.MessageEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SendMessageTest {
    @Test
    void checkValidation_expectDoesNotThrow_ValidParseAndNullEntities() {
        //given
        SendMessage sendMessage = SendMessage.builder()
                .chatId("chatId")
                .text("text")
                .parseMode("HTML")
                .entities(null)
                .build();

        //when
        //then
        assertDoesNotThrow(sendMessage::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndNullEntities() {
        //given
        SendMessage sendMessage = SendMessage.builder()
                .chatId("chatId")
                .text("text")
                .parseMode(null)
                .entities(null)
                .build();

        //when
        //then
        assertDoesNotThrow(sendMessage::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndValidEntities() {
        //given
        SendMessage sendMessage = SendMessage.builder()
                .chatId("chatId")
                .text("text")
                .parseMode(null)
                .entities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        assertDoesNotThrow(sendMessage::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_ProperParseModeButEntitiesIsEmpty() {
        //given
        SendMessage sendMessage = SendMessage.builder()
                .chatId("chatId")
                .text("text")
                .parseMode("HTML")
                .entities(new ArrayList<>())
                .build();

        //when
        //then
        assertDoesNotThrow(sendMessage::checkValidation);
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_WrongParseMode() {
        //given
        SendMessage sendMessage = SendMessage.builder()
                .chatId("chatId")
                .text("text")
                .parseMode("randomParseMode")
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, sendMessage::checkValidation);
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_ProperParseModeButEntitiesIsNotEmptyAndNull() {
        //given
        SendMessage sendMessage = SendMessage.builder()
                .chatId("chatId")
                .text("text")
                .parseMode("HTML")
                .entities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, sendMessage::checkValidation);
    }
}