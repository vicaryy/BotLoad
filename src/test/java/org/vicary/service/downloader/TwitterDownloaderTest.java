package org.vicary.service.downloader;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.exception.InvalidBotRequestException;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.service.file_service.TwitterFileService;
import org.vicary.service.quick_sender.QuickSender;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
class TwitterDownloaderTest {

    @Autowired
    private TwitterDownloader downloader;

    @MockBean
    private TwitterFileService twitterFileService;

    @MockBean
    private QuickSender quickSender;

    private final static File FILE_DIRECTORY = new File("/Users/vicary/desktop/folder/");

    private final static ProcessBuilder processBuilder = new ProcessBuilder();
    @AfterAll
    public static void afterAll() throws Exception {
        FileUtils.cleanDirectory(FILE_DIRECTORY);
    }

    @BeforeAll
    public static void beforeAll() {
        processBuilder.directory(FILE_DIRECTORY);
    }

    @Test
    void download_expectEquals_NormalFileRequestInMp4() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("123")
                .text("siema")
                .messageId(111)
                .build();
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://twitter.com/flumemusic/status/1576058248917942273?s=20")
                .chatId("1935527130")
                .extension("mp4")
                .premium(false)
                .multiVideoNumber(0)
                .editMessageText(editMessageText)
                .build();

        InputFile downloadedFile = InputFile.builder()
                .file(new File("/Users/vicary/desktop/folder/Flume - Live from New York.mp4"))
                .build();
        FileResponse expectedResponse = FileResponse.builder()
                .URL("https://twitter.com/flumemusic/status/1576058248917942273?s=20")
                .id("1576057842456416256")
                .extension("mp4")
                .premium(false)
                .title("Flume - Live from New York")
                .duration(10)
                .size(527349)
                .multiVideoNumber(1)
                .downloadedFile(downloadedFile)
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
        verify(twitterFileService).findByTwitterId("1576057842456416256");
    }

    @Test
    void download_expectInvalidBotApiThrow_FileRequestOver50MBInMp4() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("123")
                .text("siema")
                .messageId(111)
                .build();
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://twitter.com/LVGully/status/1694131703751594265?s=20")
                .chatId("1935527130")
                .extension("mp4")
                .premium(false)
                .multiVideoNumber(0)
                .editMessageText(editMessageText)
                .build();

        // when
        // then
        assertThrows(InvalidBotRequestException.class, () -> downloader.download(givenRequest));
    }

    @Test
    void getFileInfo_expectEquals_MultiVideoLinkButNumberIsSpecify() {
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://twitter.com/odesza/status/1683894840101322752?s=20")
                .chatId("1935527130")
                .extension("mp4")
                .premium(false)
                .multiVideoNumber(2)
                .build();

        FileResponse expectedResponse = FileResponse.builder()
                .URL("https://twitter.com/odesza/status/1683894840101322752?s=20")
                .id("1683894778726084608")
                .extension("mp4")
                .premium(false)
                .title("ODESZA - Thank you for the love on this EP! Which tracks are you guys enjoying? #2")
                .duration(9)
                .multiVideoNumber(2)
                .build();

        // when
        FileResponse actualResponse = null;
        try {
            actualResponse = downloader.getFileInfo(givenRequest, processBuilder);
        } catch (Exception ignored) {
        }

        // then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getFileInfo_expectInvalidBotApiThrow_NoVideoOnlyPhotoInRequest() {
        //given
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://twitter.com/flumemusic/status/1560030334871146496?s=20")
                .chatId("1935527130")
                .extension("mp4")
                .premium(false)
                .multiVideoNumber(0)
                .build();

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
    }

    @Test
    void getFileInfo_expectInvalidBotRequestThrow_NoVideoButOtherServiceLinkInDescription() {
        //given
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://twitter.com/PostMalone/status/1684780951216283648?s=20")
                .chatId("1935527130")
                .extension("mp4")
                .premium(false)
                .multiVideoNumber(0)
                .build();

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
    }

    @Test
    void getFileInfo_expectInvalidBotRequestThrow_MultiVideoLinkButNumberNotSpecify() {
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://twitter.com/odesza/status/1683894840101322752?s=20")
                .chatId("1935527130")
                .extension("mp4")
                .premium(false)
                .multiVideoNumber(0)
                .build();

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
    }

    @Test
    void getFileInfo_expectInvalidBotRequestThrow_MultiVideoLinkButNumberSpecifyIsTooHigh() {
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://twitter.com/odesza/status/1683894840101322752?s=20")
                .chatId("1935527130")
                .extension("mp4")
                .premium(false)
                .multiVideoNumber(6)
                .build();

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
    }
}