package org.vicary.api_request.send;

import org.junit.jupiter.api.Test;
import org.vicary.api_object.message.MessageEntity;
import org.vicary.api_request.InputFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SendAudioTest {

    @Test
    void checkValidation_expectDoesNotThrow_ValidParseAndNullEntities() {
        //given
        SendAudio sendAudio = SendAudio.builder()
                .chatId("chatId")
                .audio(new InputFile())
                .parseMode("HTML")
                .build();

        //when
        //then
        assertDoesNotThrow(sendAudio::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndNullEntities() {
        //given
        SendAudio sendAudio = SendAudio.builder()
                .chatId("chatId")
                .audio(new InputFile())
                .build();

        //when
        //then
        assertDoesNotThrow(sendAudio::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndValidEntities() {
        //given
        SendAudio sendAudio = SendAudio.builder()
                .chatId("chatId")
                .audio(new InputFile())
                .captionEntities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        assertDoesNotThrow(sendAudio::checkValidation);
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_WrongParseMode() {
        //given
        SendAudio sendAudio = SendAudio.builder()
                .chatId("chatId")
                .audio(new InputFile())
                .parseMode("random parse")
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, sendAudio::checkValidation);
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_ProperParseModeButEntitiesIsNotEmptyAndNull() {
        //given
        SendAudio sendAudio = SendAudio.builder()
                .chatId("chatId")
                .audio(new InputFile())
                .parseMode("HTML")
                .captionEntities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, sendAudio::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_ProperParseModeButEntitiesIsEmpty() {
        //given
        SendAudio sendAudio = SendAudio.builder()
                .chatId("chatId")
                .audio(new InputFile())
                .parseMode("HTML")
                .captionEntities(new ArrayList<>())
                .build();

        //when
        //then
        assertDoesNotThrow(sendAudio::checkValidation);
    }
}