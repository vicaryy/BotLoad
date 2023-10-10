package org.vicary.api_request.send;

import org.junit.jupiter.api.Test;
import org.vicary.api_object.message.MessageEntity;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SendVideoTest {

    @Test
    void checkValidation_expectDoesNotThrow_ValidParseAndNullEntities() {
        //given
        SendVideo sendVideo = SendVideo.builder()
                .chatId("chatId")
                .video(new InputFile())
                .parseMode("HTML")
                .build();

        //when
        //then
        assertDoesNotThrow(sendVideo::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndNullEntities() {
        //given
        SendVideo sendVideo = SendVideo.builder()
                .chatId("chatId")
                .video(new InputFile())
                .build();

        //when
        //then
        assertDoesNotThrow(sendVideo::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_NullParseAndValidEntities() {
        //given
        SendVideo sendVideo = SendVideo.builder()
                .chatId("chatId")
                .video(new InputFile())
                .captionEntities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        assertDoesNotThrow(sendVideo::checkValidation);
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_WrongParseMode() {
        //given
        SendVideo sendVideo = SendVideo.builder()
                .chatId("chatId")
                .video(new InputFile())
                .parseMode("random parse")
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, sendVideo::checkValidation);
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_ProperParseModeButEntitiesIsNotEmptyAndNull() {
        //given
        SendVideo sendVideo = SendVideo.builder()
                .chatId("chatId")
                .video(new InputFile())
                .parseMode("HTML")
                .captionEntities(new ArrayList<>(List.of(new MessageEntity())))
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, sendVideo::checkValidation);
    }

    @Test
    void checkValidation_expectDoesNotThrow_ProperParseModeButEntitiesIsEmpty() {
        //given
        SendVideo sendVideo = SendVideo.builder()
                .chatId("chatId")
                .video(new InputFile())
                .parseMode("HTML")
                .captionEntities(new ArrayList<>())
                .build();

        //when
        //then
        assertDoesNotThrow(sendVideo::checkValidation);
    }
}