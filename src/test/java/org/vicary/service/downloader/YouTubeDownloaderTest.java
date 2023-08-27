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
import org.vicary.service.file_service.YouTubeFileService;
import org.vicary.service.quick_sender.QuickSender;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
class YouTubeDownloaderTest {

    @Autowired
    private YouTubeDownloader downloader;

    @MockBean
    private QuickSender quickSender;

    @MockBean
    private YouTubeFileService youTubeFileService;

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
    void download_expectEquals_NormalFileRequestInMp3() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("123")
                .text("siema")
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
        verify(youTubeFileService).findByYoutubeIdAndExtensionAndQuality(
                "psNARNT1Y2Q",
                "mp3",
                "standard");
    }

    @Test
    void download_expectThrow_TooBigFileRequestInMp3() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("123")
                .text("siema")
                .messageId(111)
                .build();
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://youtu.be/JSg0dytYbWA?si=yPpHE6MgfqXyo9KX")
                .chatId("1935527130")
                .extension("mp3")
                .premium(false)
                .multiVideoNumber(0)
                .editMessageText(editMessageText)
                .build();

        // when
        // then
        assertThrows(InvalidBotRequestException.class, () -> downloader.download(givenRequest));
        verify(youTubeFileService).findByYoutubeIdAndExtensionAndQuality(
                "JSg0dytYbWA",
                "mp3",
                "standard");
    }

    @Test
    void download_expectEquals_YoutubeShortsFileRequestInMp3() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("123")
                .text("siema")
                .messageId(111)
                .build();
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://youtube.com/shorts/UgFxDoOpL6E?si=6lRls5MeWmT_7pUY")
                .chatId("1935527130")
                .extension("mp3")
                .premium(false)
                .multiVideoNumber(0)
                .editMessageText(editMessageText)
                .build();

        InputFile downloadedFile = InputFile.builder()
                .file(new File("/Users/vicary/desktop/folder/Flume - Dusky Coral Pea (ID).mp3"))
                .build();
        InputFile thumbnail = InputFile.builder()
                .file(new File("/Users/vicary/desktop/folder/Flume - Dusky Coral Pea (ID).jpg"))
                .isThumbnail(true)
                .build();
        FileResponse expectedResponse = FileResponse.builder()
                .URL("https://www.youtube.com/watch?v=UgFxDoOpL6E")
                .id("UgFxDoOpL6E")
                .extension("mp3")
                .premium(false)
                .title("Flume - Dusky Coral Pea (ID)")
                .duration(41)
                .size(1111977)
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
        verify(youTubeFileService).findByYoutubeIdAndExtensionAndQuality(
                "UgFxDoOpL6E",
                "mp3",
                "standard");
    }

    @Test
    void getFileInfo_expectEquals_ArtistAlbumEtcFileRequestInMp3() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("123")
                .text("siema")
                .messageId(111)
                .build();
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://www.youtube.com/watch?v=UB6sXiZ1ldw&list=PL8YH4mOwWryUMna911yJM2B52iIIzigKy&index=13")
                .chatId("1935527130")
                .extension("mp3")
                .premium(false)
                .multiVideoNumber(0)
                .editMessageText(editMessageText)
                .build();

        FileResponse expectedResponse = FileResponse.builder()
                .URL("https://www.youtube.com/watch?v=UB6sXiZ1ldw")
                .id("UB6sXiZ1ldw")
                .extension("mp3")
                .premium(false)
                .title("Who Will Survive In America")
                .duration(98)
                .artist("Kanye West")
                .track("Who Will Survive In America")
                .album("My Beautiful Dark Twisted Fantasy")
                .releaseYear("2010")
                .multiVideoNumber(0)
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
    void getFileInfo_expectThrow_InvalidURLInMp3() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("123")
                .text("siema")
                .messageId(111)
                .build();
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://youtu.be/wsaAsNT1Y2Q?si=ApAseIvSQ-xSrnTi")
                .chatId("1935527130")
                .extension("mp3")
                .premium(false)
                .multiVideoNumber(0)
                .editMessageText(editMessageText)
                .build();

        // then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
    }
}