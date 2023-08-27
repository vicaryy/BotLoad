package org.vicary.service.downloader;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.exception.InvalidBotRequestException;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.service.file_service.TikTokFileService;
import org.vicary.service.quick_sender.QuickSender;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@RunWith(Runner.class)
@SpringBootTest
class TikTokDownloaderTest {
    @Autowired
    private TikTokDownloader downloader;
    @MockBean
    private QuickSender quickSender;
    @MockBean
    private TikTokFileService tiktokFileService;

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
                .URL("https://www.tiktok.com/@kexnjii/video/7178592761849875717?is_from_webapp=1&sender_device=pc&web_id=7252294415770289690")
                .chatId("1935527130")
                .extension("mp4")
                .premium(false)
                .multiVideoNumber(0)
                .editMessageText(editMessageText)
                .build();

        InputFile downloadedFile = InputFile.builder()
                .file(new File("/Users/vicary/desktop/folder/Let’s go Speed \uD83C\uDDE6\uD83C\uDDF7\uD83E\uDEF6\uD83C\uDFFB #ishowspeed #ishowspeedclipz #ishow.mp4"))
                .build();
        FileResponse expectedResponse = FileResponse.builder()
                .URL("https://www.tiktok.com/@6778504638314595333/video/7178592761849875717")
                .id("7178592761849875717")
                .extension("mp4")
                .premium(false)
                .title("Let’s go Speed \uD83C\uDDE6\uD83C\uDDF7\uD83E\uDEF6\uD83C\uDFFB #ishowspeed #ishowspeedclipz #ishowsspeedclip #worldcup #worldcup2022 #argentina #kexnjii ")
                .duration(20)
                .size(2709260)
                .artist("The King Khan & BBQ Show")
                .track("Love You So")
                .album("Love You So")
                .multiVideoNumber(0)
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
        verify(tiktokFileService).findByTikTokId("7178592761849875717");
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
                .URL("https://www.tiktok.com/@fridaasmr7/video/7235488413502164250?q=1%20hour%20video&t=1693159500559")
                .chatId("1935527130")
                .extension("mp4")
                .premium(false)
                .multiVideoNumber(0)
                .editMessageText(editMessageText)
                .build();


        // when
        // then
        assertThrows(InvalidBotRequestException.class, () -> downloader.download(givenRequest));
        verify(tiktokFileService).findByTikTokId("7235488413502164250");
    }

    @Test
    void getFileInfo_expectInvalidBotApiThrow_InvalidURLInFileRequestInMp4() {
        //given
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId("123")
                .text("siema")
                .messageId(111)
                .build();
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://www.tiktok.com/@fridaasmr7/video/2335488413502164250?q=1%20hour%20video&t=1693159500559")
                .chatId("1935527130")
                .extension("mp4")
                .premium(false)
                .multiVideoNumber(0)
                .editMessageText(editMessageText)
                .build();


        // when
        // then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
    }
}