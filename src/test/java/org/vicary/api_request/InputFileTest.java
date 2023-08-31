package org.vicary.api_request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InputFileTest {

    private static InputFile inputFile;

    @BeforeAll
    static void beforeAll() {
        File file = mock(File.class);
        inputFile = new InputFile();
        inputFile.setFile(file);
    }

    @Test
    void checkValidation_expectDoesNotThrow_ValidParamsWithFileId() {
        // given
        InputFile inputFile = InputFile.builder()
                .fileId("fileId")
                .build();

        // when
        // then
        assertDoesNotThrow(() -> inputFile.checkValidation("something"));
    }

    @Test
    void checkValidation_expectDoesNotThrow_ValidParamsWithFile() {
        //given
        String methodName = "audio";
        InputFile inputFile = InputFile.builder()
                .file(new File("/someFileInMp3.mp3"))
                .build();

        //when
        //then
        assertDoesNotThrow(() -> inputFile.checkValidation(methodName));
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_FileAndFileIdAreNotNull() {
        //given
        String methodName = "audio";
        InputFile inputFile = InputFile.builder()
                .file(new File("/someFileInMp3.mp3"))
                .fileId("fileId")
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.checkValidation(methodName));
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_FileAndFileIdAreNull() {
        //given
        String methodName = "audio";
        InputFile inputFile = InputFile.builder()
                .file(null)
                .fileId(null)
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.checkValidation(methodName));
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_ThumbnailButWithFileId() {
        //given
        String methodName = "thumbnail";
        InputFile inputFile = InputFile.builder()
                .file(null)
                .fileId("fileId")
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.checkValidation(methodName));
    }

    @Test
    void checkValidation_expectThrowIllegalArgumentEx_FileTooBigOver50MB() {
        //given
        String methodName = "video";

        //when
        when(inputFile.getFile().length()).thenReturn(100000000L);
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.checkValidation(methodName));
    }

    @Test
    void photoValidation_expectDoesNotThrow_ValidFileName() {
        //given
        String fileName = "photo123.jpg";

        //when
        //then
        assertDoesNotThrow(() -> inputFile.photoValidation(fileName));
    }

    @Test
    void photoValidation_expectThrowIllegalArgumentEx_InvalidFileName() {
        //given
        String fileName = "photo123.mp4";

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.photoValidation(fileName));
    }

    @Test
    void videoValidation_expectDoesNotThrow_ValidFileName() {
        //given
        String fileName = "video123.avi";

        //when
        //then
        assertDoesNotThrow(() -> inputFile.videoValidation(fileName));
    }

    @Test
    void videoValidation_expectThrowIllegalArgumentEx_InvalidFileName() {
        //given
        String fileName = "video.jpeg";

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.videoValidation(fileName));
    }


    @Test
    void audioValidation_expectDoesNotThrow_ValidFileName() {
        //given
        String fileName = "audio123.mp3";

        //when
        //then
        assertDoesNotThrow(() -> inputFile.audioValidation(fileName));
    }

    @Test
    void audioValidation_expectThrowIllegalArgumentEx_InvalidFileName() {
        //given
        String fileName = "audio.ogg";

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.audioValidation(fileName));
    }


    @Test
    void animationValidation_expectDoesNotThrow_ValidFileName() {
        //given
        String fileName = "animation123.gif";

        //when
        //then
        assertDoesNotThrow(() -> inputFile.animationValidation(fileName));
    }

    @Test
    void animationValidation_expectThrowIllegalArgumentEx_InvalidFileName() {
        //given
        String fileName = "animation.jpeg";

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.animationValidation(fileName));
    }


    @Test
    void voiceValidation_expectDoesNotThrow_ValidFileName() {
        //given
        String fileName = "voice123.ogg";

        //when
        //then
        assertDoesNotThrow(() -> inputFile.voiceValidation(fileName));
    }

    @Test
    void voiceValidation_expectThrowIllegalArgumentEx_InvalidFileName() {
        //given
        String fileName = "voice.jpeg";

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.voiceValidation(fileName));
    }


    @Test
    void stickerValidation_expectDoesNotThrow_ValidFileName() {
        //given
        String fileName = "sticker123.webm";

        //when
        //then
        assertDoesNotThrow(() -> inputFile.stickerValidation(fileName));
    }

    @Test
    void stickerValidation_expectThrowIllegalArgumentEx_InvalidFileName() {
        //given
        String fileName = "sticker.mp3";

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.stickerValidation(fileName));
    }

    @Test
    void thumbnailValidation_expectDoesNotThrow_ValidFileName() {
        //given
        String fileName = "thumbnail123.jpg";

        //when
        when(inputFile.getFile().length()).thenReturn(1L);
        //then
        assertDoesNotThrow(() -> inputFile.thumbnailValidation(fileName));
    }

    @Test
    void thumbnailValidation_expectThrowIllegalArgumentEx_InvalidFileName() {
        //given
        String fileName = "thumbnail.mp3";

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.thumbnailValidation(fileName));
    }

    @Test
    void thumbnailValidation_expectThrowIllegalArgumentEx_ThumbnailTooBigOver200kB() {
        //given
        String fileName = "thumbnail.mp3";

        //when
        when(inputFile.getFile().length()).thenReturn(400000L);
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.thumbnailValidation(fileName));
    }
}
































