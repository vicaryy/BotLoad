package org.vicary.api_request;

import org.aspectj.util.FileUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InputFileTest {

    private static File FILE_NORMAL;
    private static File FILE_OVER_50MB;
    private static File FILE_THUMBNAIL;

    @BeforeAll
    public static void beforeAll() throws Exception {
        FILE_NORMAL = new File("file_normal.mp3");
        byte[] bytesForNormalFile = new byte[1000000];
        try (OutputStream outputStream = new FileOutputStream(FILE_NORMAL)) {
            outputStream.write(bytesForNormalFile);
        }

        FILE_OVER_50MB = new File("file_over_50MB.mp3");
        byte[] bytesForOver50MBFile = new byte[55000000];
        try (OutputStream outputStream = new FileOutputStream(FILE_OVER_50MB)) {
            outputStream.write(bytesForOver50MBFile);
        }

        FILE_THUMBNAIL = new File("file_thumbnail.jpg");
        byte[] bytesForThumbnailFile = new byte[100];
        try (OutputStream outputStream = new FileOutputStream(FILE_THUMBNAIL)) {
            outputStream.write(bytesForThumbnailFile);
        }
    }

    @AfterAll
    public static void afterAll() {
        FileUtil.deleteContents(FILE_NORMAL);
        FileUtil.deleteContents(FILE_OVER_50MB);
        FileUtil.deleteContents(FILE_THUMBNAIL);
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
                .file(FILE_NORMAL)
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
                .file(FILE_NORMAL)
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
        InputFile inputFile = InputFile.builder()
                .file(FILE_OVER_50MB)
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.checkValidation(methodName));
    }

    @Test
    void photoValidation_expectDoesNotThrow_ValidFileName() {
        //given
        String fileName = "photo123.jpg";
        InputFile inputFile = InputFile.builder()
                .file(FILE_NORMAL)
                .build();

        //when
        //then
        assertDoesNotThrow(() -> inputFile.photoValidation(fileName));
    }

    @Test
    void photoValidation_expectThrowIllegalArgumentEx_InvalidFileName() {
        //given
        String fileName = "photo123.mp4";
        InputFile inputFile = InputFile.builder()
                .file(FILE_NORMAL)
                .build();
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.photoValidation(fileName));
    }

    @Test
    void videoValidation_expectDoesNotThrow_ValidFileName() {
        //given
        String fileName = "video123.avi";
        InputFile inputFile = InputFile.builder()
                .file(FILE_NORMAL)
                .build();

        //when
        //then
        assertDoesNotThrow(() -> inputFile.videoValidation(fileName));
    }

    @Test
    void videoValidation_expectThrowIllegalArgumentEx_InvalidFileName() {
        //given
        String fileName = "video.jpeg";
        InputFile inputFile = InputFile.builder()
                .file(FILE_NORMAL)
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.videoValidation(fileName));
    }


    @Test
    void audioValidation_expectDoesNotThrow_ValidFileName() {
        //given
        String fileName = "audio123.mp3";
        InputFile inputFile = InputFile.builder()
                .file(FILE_NORMAL)
                .build();

        //when
        //then
        assertDoesNotThrow(() -> inputFile.audioValidation(fileName));
    }

    @Test
    void audioValidation_expectThrowIllegalArgumentEx_InvalidFileName() {
        //given
        String fileName = "audio.ogg";
        InputFile inputFile = InputFile.builder()
                .file(FILE_NORMAL)
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.audioValidation(fileName));
    }


    @Test
    void animationValidation_expectDoesNotThrow_ValidFileName() {
        //given
        String fileName = "animation123.gif";
        InputFile inputFile = InputFile.builder()
                .file(FILE_NORMAL)
                .build();

        //when
        //then
        assertDoesNotThrow(() -> inputFile.animationValidation(fileName));
    }

    @Test
    void animationValidation_expectThrowIllegalArgumentEx_InvalidFileName() {
        //given
        String fileName = "animation.jpeg";
        InputFile inputFile = InputFile.builder()
                .file(FILE_NORMAL)
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.animationValidation(fileName));
    }


    @Test
    void voiceValidation_expectDoesNotThrow_ValidFileName() {
        //given
        String fileName = "voice123.ogg";
        InputFile inputFile = InputFile.builder()
                .file(FILE_NORMAL)
                .build();

        //when
        //then
        assertDoesNotThrow(() -> inputFile.voiceValidation(fileName));
    }

    @Test
    void voiceValidation_expectThrowIllegalArgumentEx_InvalidFileName() {
        //given
        String fileName = "voice.jpeg";
        InputFile inputFile = InputFile.builder()
                .file(FILE_NORMAL)
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.voiceValidation(fileName));
    }


    @Test
    void stickerValidation_expectDoesNotThrow_ValidFileName() {
        //given
        String fileName = "sticker123.webm";
        InputFile inputFile = InputFile.builder()
                .file(FILE_NORMAL)
                .build();

        //when
        //then
        assertDoesNotThrow(() -> inputFile.stickerValidation(fileName));
    }

    @Test
    void stickerValidation_expectThrowIllegalArgumentEx_InvalidFileName() {
        //given
        String fileName = "sticker.mp3";
        InputFile inputFile = InputFile.builder()
                .file(FILE_NORMAL)
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.stickerValidation(fileName));
    }

    @Test
    void thumbnailValidation_expectDoesNotThrow_ValidFileName() {
        //given
        String fileName = "thumbnail123.jpg";
        InputFile inputFile = InputFile.builder()
                .file(FILE_THUMBNAIL)
                .build();

        //when
        //then
        assertDoesNotThrow(() -> inputFile.thumbnailValidation(fileName));
    }

    @Test
    void thumbnailValidation_expectThrowIllegalArgumentEx_InvalidFileName() {
        //given
        String fileName = "thumbnail.mp3";
        InputFile inputFile = InputFile.builder()
                .file(FILE_NORMAL)
                .build();

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.thumbnailValidation(fileName));
    }

    @Test
    void thumbnailValidation_expectThrowIllegalArgumentEx_ThumbnailTooBigOver200kB() {
        //given
        String fileName = "thumbnail.mp3";
        InputFile inputFile = InputFile.builder()
                .file(FILE_OVER_50MB)
                .build();

        //then
        assertThrows(IllegalArgumentException.class, () -> inputFile.thumbnailValidation(fileName));
    }
}
































