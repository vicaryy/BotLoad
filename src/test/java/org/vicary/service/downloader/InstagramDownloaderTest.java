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
import org.vicary.service.file_service.InstagramFileService;
import org.vicary.service.quick_sender.QuickSender;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
class InstagramDownloaderTest {

    @Autowired
    private InstagramDownloader downloader;

    @MockBean
    private InstagramFileService instagramFileService;

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
                .URL("https://www.instagram.com/p/BRyPt1sDGfG/?utm_source=ig_web_copy_link&igshid=MzRlODBiNWFlZA==")
                .chatId("1935527130")
                .extension("mp4")
                .premium(false)
                .multiVideoNumber(0)
                .editMessageText(editMessageText)
                .build();

        InputFile downloadedFile = InputFile.builder()
                .file(new File("/Users/vicary/desktop/folder/Video by _vicary_.mp4"))
                .build();
        FileResponse expectedResponse = FileResponse.builder()
                .URL("https://www.instagram.com/p/BRyPt1sDGfG/?utm_source=ig_web_copy_link&igshid=MzRlODBiNWFlZA==")
                .id("BRyPt1sDGfG")
                .extension("mp4")
                .premium(false)
                .title("Video by _vicary_")
                .duration(14)
                .size(1460252)
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
        verify(instagramFileService).findByInstagramId("BRyPt1sDGfG");
    }

    @Test
    void download_expectInvalidBotApiThrow_FileRequestOver50MBInMp4() {
    }

    @Test
    void getFileInfo_expectEquals_MultiVideoLinkButNumberIsSpecify() {
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://www.instagram.com/p/Cv-ZNz9RTzw/?utm_source=ig_web_copy_link&igshid=MzRlODBiNWFlZA%3D%3D")
                .chatId("1935527130")
                .extension("mp4")
                .premium(false)
                .multiVideoNumber(5)
                .build();

        FileResponse expectedResponse = FileResponse.builder()
                .URL("https://www.instagram.com/p/Cv-ZNz9RTzw/?utm_source=ig_web_copy_link&igshid=MzRlODBiNWFlZA%3D%3D")
                .id("Cv-XIV8gMtx")
                .extension("mp4")
                .premium(false)
                .title("Video 7")
                .duration(0)
                .multiVideoNumber(5)
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
    void getFileInfo_expectInvalidBotRequestThrow_NoVideoOnlyPhotoInRequest() {
        //given
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://www.instagram.com/p/Ct7DxqSRRYG/?utm_source=ig_web_copy_link&igshid=MzRlODBiNWFlZA==")
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
                .URL("https://www.instagram.com/p/Cv-ZNz9RTzw/?utm_source=ig_web_copy_link&igshid=MzRlODBiNWFlZA%3D%3D")
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
                .URL("https://www.instagram.com/p/Cv-ZNz9RTzw/?utm_source=ig_web_copy_link&igshid=MzRlODBiNWFlZA%3D%3D")
                .chatId("1935527130")
                .extension("mp4")
                .premium(false)
                .multiVideoNumber(10)
                .build();

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
    }
}




























