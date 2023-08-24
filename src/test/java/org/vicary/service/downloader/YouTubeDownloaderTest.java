package org.vicary.service.downloader;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.service.file_service.YouTubeFileService;
import org.vicary.service.quick_sender.QuickSender;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class YouTubeDownloaderTest {

    private final YouTubeDownloader downloader;

    @MockBean
    private final QuickSender quickSender;

    @MockBean
    private final YouTubeFileService youTubeFileService;

    @Autowired
    public YouTubeDownloaderTest(YouTubeDownloader downloader) {
        this.downloader = downloader;
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void download_expectEquals_NormalFileRequest() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("123")
                .text("")
                .messageId(111)
                .build();
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://youtu.be/psNARNT1Y2Q?si=ApAseIvSQ-xSrnTi")
                .chatId("1935527130")
                .extension("mp3")
                .premium(false)
                .multiVideoNumber(0)
                .editMessageText(editMessageText)
                .build();

        InputFile downloadedFile = InputFile.builder()
                .file(new File("/Users/vicary/desktop/folder/Bradley Martyn Wrestling Brian Shaw.mp3"))
                .build();
        InputFile thumbnail = InputFile.builder()
                .file(new File("/Users/vicary/desktop/folder/Bradley Martyn Wrestling Brian Shaw.jpg"))
                .isThumbnail(true)
                .build();
        FileResponse expectedResponse = FileResponse.builder()
                .URL("https://www.youtube.com/watch?v=psNARNT1Y2Q")
                .id("psNARNT1Y2Q")
                .extension("mp3")
                .premium(false)
                .title("Bradley Martyn Wrestling Brian Shaw")
                .duration(20)
                .size(916446)
                .multiVideoNumber(0)
                .downloadedFile(downloadedFile)
                .thumbnail(thumbnail)
                .editMessageText(editMessageText)
                .build();

        // when
        FileResponse actualResponse = null;
        try {
            actualResponse = downloader.download(givenRequest);
        } catch (Exception ignored) {
        }

        // then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getFileInfo() {
    }
}