package org.vicary.api_request.send;

import org.junit.jupiter.api.Test;
import org.vicary.api_object.message.MessageEntity;
import org.vicary.api_request.InputFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SendDocumentTest {

    @Test
    void checkValidation_expectDoesNotThrow_ValidParseAndNullEntities() {
        //given
        SendDocument sendDocument = SendDocument.builder()
                .chatId("chatId")
                .document(new InputFile())
                .parseMode("HTML")
                .build();

        //when
        //then
        assertDoesNotThrow(sendDocument::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndNullEntities() {
        //given
        SendDocument sendDocument = SendDocument.builder()
                .chatId("chatId")
                .document(new InputFile())
                .build();

        //when
        //then
        assertDoesNotThrow(sendDocument::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndValidEntities() {
        //given
        SendDocument sendDocument = SendDocument.builder()
                .chatId("chatId")
                .document(new InputFile())
                .captionEntities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        assertDoesNotThrow(sendDocument::checkValidation);
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_WrongParseMode() {
        //given
        SendDocument sendDocument = SendDocument.builder()
                .chatId("chatId")
                .document(new InputFile())
                .parseMode("random parse")
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, sendDocument::checkValidation);
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_ProperParseModeButEntitiesIsNotEmptyAndNull() {
        //given
        SendDocument sendDocument = SendDocument.builder()
                .chatId("chatId")
                .document(new InputFile())
                .parseMode("HTML")
                .captionEntities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, sendDocument::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_ProperParseModeButEntitiesIsEmpty() {
        //given
        SendDocument sendDocument = SendDocument.builder()
                .chatId("chatId")
                .document(new InputFile())
                .parseMode("HTML")
                .captionEntities(new ArrayList<>())
                .build();

        //when
        //then
        assertDoesNotThrow(sendDocument::checkValidation);
    }
}