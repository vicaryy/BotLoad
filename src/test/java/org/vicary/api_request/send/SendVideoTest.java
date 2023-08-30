package org.vicary.api_request.send;

import org.junit.jupiter.api.Test;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SendVideoTest {
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
    void checkValidation_expectThrowIllegalArgumentEx_ProperParseModeButEntitiesIsSpecify() {
        //given
        SendVideo sendVideo = SendVideo.builder()
                .chatId("chatId")
                .video(new InputFile())
                .parseMode("wrong parse")
                .e
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, sendvideo::checkValidation);
    }
}