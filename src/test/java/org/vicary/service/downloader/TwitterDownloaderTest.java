package org.vicary.service.downloader;

import com.google.gson.Gson;
import org.aspectj.util.FileUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.command.YtDlpCommand;
import org.vicary.entity.TwitterFileEntity;
import org.vicary.exception.DownloadedFileNotFoundException;
import org.vicary.exception.InvalidBotRequestException;
import org.vicary.info.DownloaderInfo;
import org.vicary.model.FileInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.pattern.Pattern;
import org.vicary.service.Converter;
import org.vicary.service.file_service.TwitterFileService;
import org.vicary.service.mapper.FileInfoMapper;
import org.vicary.service.quick_sender.QuickSender;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class TwitterDownloaderTest {

    @Autowired
    private TwitterDownloader downloader;

    @MockBean
    private TwitterFileService fileService;

    @MockBean
    private FileInfoMapper mapper;

    @MockBean
    private DownloaderInfo info;

    @MockBean
    private QuickSender quickSender;

    @MockBean
    private Gson gson;

    @MockBean
    private Pattern pattern;

    @MockBean
    private Converter converter;

    @MockBean
    private DownloaderManager downloaderManager;

    @MockBean
    private YtDlpCommand commands;

    private final static ProcessBuilder processBuilder = new ProcessBuilder();
    private static File FILE_NORMAL;
    private static File FILE_OVER_50MB;

    @BeforeAll
    public static void beforeAll() throws IOException {
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
    }


    @AfterAll
    public static void afterAll() {
        FileUtil.deleteContents(FILE_NORMAL);
        FileUtil.deleteContents(FILE_OVER_50MB);
    }

    @Test
    void downloadFile_expectEquals_NormalFileResponse() throws IOException {
        //given
        String[] givenCommand = {"echo", "hello"};
        EditMessageText givenEMT = new EditMessageText("chatId", 123, "text");
        String givenServiceId = "12345";
        String givenTitle = "file_normal";
        String givenExtension = "mp3";
        String givenFilePath = "file_normal.mp3";
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenServiceId)
                .title(givenTitle)
                .extension(givenExtension)
                .editMessageText(givenEMT)
                .build();

        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenServiceId)
                .title(givenTitle)
                .editMessageText(givenEMT)
                .size(FILE_NORMAL.length())
                .extension(givenExtension)
                .downloadedFile(InputFile.builder()
                        .file(FILE_NORMAL)
                        .build())
                .build();

        //when
        when(downloaderManager.getFileNameFromTitle(givenTitle, givenExtension)).thenReturn(givenFilePath);
        when(commands.getDownloadDestination()).thenReturn("");
        when(info.getFileDownloading()).thenReturn("");
        when(commands.downloadTwitter(givenFilePath, givenFileResponse)).thenReturn(givenCommand);
        when(downloaderManager.isFileDownloadingInProcess(givenCommand[1])).thenReturn(true);
        when(downloaderManager.isFileSizeValidInProcess(givenCommand[1])).thenReturn(true);
        when(downloaderManager.isFileConvertingInProcess(givenCommand[1])).thenReturn(true);
        when(downloaderManager.isFileSizeValid(FILE_NORMAL.length())).thenReturn(true);

        FileResponse actualFileResponse = downloader.downloadFile(givenFileResponse, processBuilder);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);

        verify(quickSender, times(2)).editMessageText(any(), any());
        verify(downloaderManager).updateDownloadProgressInEditMessageText(givenEMT, givenCommand[1]);
        verify(downloaderManager).isFileDownloadedInProcess(givenCommand[1]);
        verify(downloaderManager).isFileSizeValidInProcess(givenCommand[1]);
        verify(downloaderManager).isFileConvertingInProcess(givenCommand[1]);
        verify(downloaderManager).isFileSizeValid(FILE_NORMAL.length());
    }


    @Test
    void downloadFile_expectThrow_FileOver50MB() {
        //given
        String[] givenCommand = {"echo", "hello"};
        EditMessageText givenEMT = new EditMessageText("chatId", 123, "text");
        String givenServiceId = "12345";
        String givenTitle = "file_over_50MB";
        String givenExtension = "mp3";
        String givenFilePath = "file_over_50MB.mp3";
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenServiceId)
                .title(givenTitle)
                .extension(givenExtension)
                .editMessageText(givenEMT)
                .build();

        //when
        when(downloaderManager.getFileNameFromTitle(givenTitle, givenExtension)).thenReturn(givenFilePath);
        when(commands.getDownloadDestination()).thenReturn("");
        when(info.getFileDownloading()).thenReturn("");
        when(commands.downloadTwitter(givenFilePath, givenFileResponse)).thenReturn(givenCommand);
        when(downloaderManager.isFileDownloadingInProcess(givenCommand[1])).thenReturn(true);
        when(downloaderManager.isFileSizeValidInProcess(givenCommand[1])).thenReturn(true);
        when(downloaderManager.isFileConvertingInProcess(givenCommand[1])).thenReturn(true);
        when(downloaderManager.isFileSizeValid(FILE_OVER_50MB.length())).thenReturn(false);

        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.downloadFile(givenFileResponse, processBuilder));

        verify(quickSender, times(2)).editMessageText(any(), any());
        verify(downloaderManager).updateDownloadProgressInEditMessageText(givenEMT, givenCommand[1]);
        verify(downloaderManager).isFileDownloadedInProcess(givenCommand[1]);
        verify(downloaderManager).isFileSizeValidInProcess(givenCommand[1]);
        verify(downloaderManager).isFileConvertingInProcess(givenCommand[1]);
        verify(downloaderManager).isFileSizeValid(FILE_OVER_50MB.length());
    }


    @Test
    void downloadFile_expectThrow_FileOver50MBInProcess() {
        //given
        String[] givenCommand = {"echo", "hello"};
        EditMessageText givenEMT = new EditMessageText("chatId", 123, "text");
        String givenServiceId = "12345";
        String givenTitle = "file_over_50MB";
        String givenExtension = "mp3";
        String givenFilePath = "file_over_50MB.mp3";
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenServiceId)
                .title(givenTitle)
                .extension(givenExtension)
                .editMessageText(givenEMT)
                .build();

        //when
        when(downloaderManager.getFileNameFromTitle(givenTitle, givenExtension)).thenReturn(givenFilePath);
        when(commands.getDownloadDestination()).thenReturn("");
        when(info.getFileDownloading()).thenReturn("");
        when(commands.downloadTwitter(givenFilePath, givenFileResponse)).thenReturn(givenCommand);
        when(downloaderManager.isFileDownloadingInProcess(givenCommand[1])).thenReturn(true);
        when(downloaderManager.isFileSizeValidInProcess(givenCommand[1])).thenReturn(false);


        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.downloadFile(givenFileResponse, processBuilder));

        verify(quickSender, atMostOnce()).editMessageText(any(), any());
        verify(downloaderManager).updateDownloadProgressInEditMessageText(givenEMT, givenCommand[1]);
        verify(downloaderManager).isFileDownloadedInProcess(givenCommand[1]);
        verify(downloaderManager).isFileSizeValidInProcess(givenCommand[1]);
    }


    @Test
    void downloadFile_expectThrow_FileNotDownloaded() {
        //given
        String[] givenCommand = {"echo", "hello"};
        EditMessageText givenEMT = new EditMessageText("chatId", 123, "text");
        String givenServiceId = "12345";
        String givenTitle = "file_not_exist";
        String givenExtension = "mp3";
        String givenFilePath = "file_not_exist.mp3";
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenServiceId)
                .title(givenTitle)
                .extension(givenExtension)
                .editMessageText(givenEMT)
                .build();

        //when
        when(downloaderManager.getFileNameFromTitle(givenTitle, givenExtension)).thenReturn(givenFilePath);
        when(commands.getDownloadDestination()).thenReturn("");
        when(info.getFileDownloading()).thenReturn("");
        when(commands.downloadTwitter(givenFilePath, givenFileResponse)).thenReturn(givenCommand);
        when(downloaderManager.isFileDownloadingInProcess(givenCommand[1])).thenReturn(true);
        when(downloaderManager.isFileSizeValidInProcess(givenCommand[1])).thenReturn(true);
        when(downloaderManager.isFileConvertingInProcess(givenCommand[1])).thenReturn(true);

        //then
        assertThrows(DownloadedFileNotFoundException.class, () -> downloader.downloadFile(givenFileResponse, processBuilder));

        verify(quickSender, times(2)).editMessageText(any(), any());
        verify(downloaderManager).updateDownloadProgressInEditMessageText(givenEMT, givenCommand[1]);
        verify(downloaderManager).isFileDownloadedInProcess(givenCommand[1]);
        verify(downloaderManager).isFileSizeValidInProcess(givenCommand[1]);
        verify(downloaderManager).isFileConvertingInProcess(givenCommand[1]);
    }


    @Test
    void getFileInfo_expectEquals_ValidRequest() throws IOException {
        //given
        String[] givenCommands = {"echo", "nothing"};
        String givenId = "example_id";
        String givenTitle = "title";
        String givenExtension = "mp3";
        int givenDuration = 10;
        String givenURL = "https://www.twitter.com/example__id";
        String givenChatId = "chatId";
        String givenJsonText = "nothing";
        String givenServiceName = "twitter";
        FileInfo givenFileInfo = FileInfo.builder()
                .id(givenId)
                .title(givenTitle)
                .duration(givenDuration)
                .URL(givenURL)
                .uploaderURL("twitter.com/")
                .build();
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .URL(givenURL)
                .duration(givenDuration)
                .title(givenTitle)
                .build();
        EditMessageText editMessageText = new EditMessageText(givenChatId, 123, "text");
        FileRequest givenRequest = FileRequest.builder()
                .URL(givenURL)
                .chatId(givenChatId)
                .extension(givenExtension)
                .premium(false)
                .editMessageText(editMessageText)
                .build();


        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .URL(givenURL)
                .duration(givenDuration)
                .title(givenTitle)
                .extension(givenExtension)
                .premium(false)
                .multiVideoNumber(1)
                .editMessageText(editMessageText)
                .build();
        //when
        when(commands.fileInfoTwitter(givenURL)).thenReturn(givenCommands);
        when(gson.fromJson(givenJsonText, FileInfo.class)).thenReturn(givenFileInfo);
        when(mapper.map(givenFileInfo, givenServiceName)).thenReturn(givenFileResponse);

        FileResponse actualFileResponse = downloader.getFileInfo(givenRequest, processBuilder);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(commands).fileInfoTwitter(givenURL);
        verify(gson, times(2)).fromJson(givenJsonText, FileInfo.class);
        verify(mapper).map(givenFileInfo, givenServiceName);
    }


    @Test
    void getFileInfo_expectInvalidBotRequestEx_UploadedURLNull() {
        //given
        String[] givenCommands = {"echo", "nothing"};
        String givenId = "example_id";
        String givenTitle = "title";
        String givenExtension = "mp3";
        int givenDuration = 10;
        String givenURL = "https://www.twitter.com/example__id";
        String givenChatId = "chatId";
        String givenJsonText = "nothing";
        FileInfo givenFileInfo = FileInfo.builder()
                .id(givenId)
                .title(givenTitle)
                .duration(givenDuration)
                .URL(givenURL)
                .uploaderURL(null)
                .build();
        EditMessageText editMessageText = new EditMessageText(givenChatId, 123, "text");
        FileRequest givenRequest = FileRequest.builder()
                .URL(givenURL)
                .chatId(givenChatId)
                .extension(givenExtension)
                .premium(false)
                .editMessageText(editMessageText)
                .build();

        //when
        when(commands.fileInfoTwitter(givenURL)).thenReturn(givenCommands);
        when(gson.fromJson(givenJsonText, FileInfo.class)).thenReturn(givenFileInfo);


        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
        verify(commands).fileInfoTwitter(givenURL);
        verify(gson, times(1)).fromJson(givenJsonText, FileInfo.class);
    }


    @Test
    void getFileInfo_expectInvalidBotRequestEx_UploadedURLDoesNotContainsTwitter() {
        //given
        String[] givenCommands = {"echo", "nothing"};
        String givenId = "example_id";
        String givenTitle = "title";
        String givenExtension = "mp3";
        int givenDuration = 10;
        String givenURL = "https://www.twitter.com/example__id";
        String givenChatId = "chatId";
        String givenJsonText = "nothing";
        FileInfo givenFileInfo = FileInfo.builder()
                .id(givenId)
                .title(givenTitle)
                .duration(givenDuration)
                .URL(givenURL)
                .uploaderURL("youtube.com:)")
                .build();
        EditMessageText editMessageText = new EditMessageText(givenChatId, 123, "text");
        FileRequest givenRequest = FileRequest.builder()
                .URL(givenURL)
                .chatId(givenChatId)
                .extension(givenExtension)
                .premium(false)
                .editMessageText(editMessageText)
                .build();

        //when
        when(commands.fileInfoTwitter(givenURL)).thenReturn(givenCommands);
        when(gson.fromJson(givenJsonText, FileInfo.class)).thenReturn(givenFileInfo);


        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
        verify(commands).fileInfoTwitter(givenURL);
        verify(gson, times(1)).fromJson(givenJsonText, FileInfo.class);
    }


    @Test
    void getFileInfo_expectInvalidBotRequestEx_MultiVideoButNotSpecify() {
        //given
        String[] givenCommands = {"echo", "first file info \n second file info"};
        String givenURL = "https://www.twitter.com/example__id";
        String givenFirstJsonText = "first file info ";
        String givenSecondJsonText = " second file info";
        int givenMultiVideoNumber = 0;
        FileInfo givenFileInfo = FileInfo.builder()
                .URL(givenURL)
                .uploaderURL("twitter.com/")
                .build();
        FileRequest givenRequest = FileRequest.builder()
                .URL(givenURL)
                .multiVideoNumber(givenMultiVideoNumber)
                .build();

        //when
        when(commands.fileInfoTwitter(givenURL)).thenReturn(givenCommands);
        when(gson.fromJson(givenFirstJsonText, FileInfo.class)).thenReturn(givenFileInfo);
        when(gson.fromJson(givenSecondJsonText, FileInfo.class)).thenReturn(givenFileInfo);


        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
        verify(commands).fileInfoTwitter(givenURL);
        verify(gson, times(1)).fromJson(givenFirstJsonText, FileInfo.class);
    }


    @Test
    void getFileInfo_expectInvalidBotRequestEx_MultiVideoSpecifyNumberIsAboveMax() {
        //given
        String[] givenCommands = {"echo", """
                                                first
                                                second
                                                third
                                                fourth
                                                fifth
                                                sixth
                                                seventh
                                                eighth
                                                ninth
                                                tenth
                                                eleventh
                                                twelfth
                                                thirteenth
                                                fourteenth
                                                fifteenth
                                                sixteenth
                                                seventeenth
                                                eighteenth
                                                nineteenth
                                                twentieth
                                                twenty-first
                                                twenty-second
                                                twenty-third
                                                twenty-fourth
                                                twenty-fifth
                                                twenty-sixth
                                                twenty-seventh
                                                twenty-eighth
                                                twenty-ninth
                                                thirtieth
                                                thirty-first
                                                thirty-second
                                                thirty-third
                                                thirty-fourth
                                                thirty-fifth
                                                thirty-sixth
                                                thirty-seventh
                                                thirty-eighth
                                                thirty-ninth
                                                fortieth
                                                forty-first
                                                forty-second
                                                forty-third
                                                forty-fourth
                                                forty-fifth
                                                forty-sixth
                                                forty-seventh
                                                forty-eighth
                                                forty-ninth
                                                fiftieth
                                                fifty-first"""};
        String givenURL = "https://www.twitter.com/example__id";

        int givenMultiVideoNumber = 55;

        FileInfo givenFileInfo = FileInfo.builder()
                .uploaderURL("twitter.com/")
                .build();
        FileRequest givenRequest = FileRequest.builder()
                .URL(givenURL)
                .multiVideoNumber(givenMultiVideoNumber)
                .build();

        //when
        when(commands.fileInfoTwitter(givenURL)).thenReturn(givenCommands);
        when(gson.fromJson(any(String.class), eq(FileInfo.class))).thenReturn(givenFileInfo);


        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
        verify(commands).fileInfoTwitter(givenURL);
        verify(gson, times(16)).fromJson(any(String.class), eq(FileInfo.class));
    }


    @Test
    void getFileInfo_expectInvalidBotRequestEx_NoVideoInURL() {
        //given
        String[] givenCommands = {"ls", ">/dev/null"};
        String givenURL = "https://www.twitter.com/NO_VIDEO_URL";
        String givenJsonText = null;
        int givenMultiVideoNumber = 1;

        FileRequest givenRequest = FileRequest.builder()
                .URL(givenURL)
                .multiVideoNumber(givenMultiVideoNumber)
                .build();

        //when
        when(commands.fileInfoTwitter(givenURL)).thenReturn(givenCommands);


        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
        verify(commands).fileInfoTwitter(givenURL);
        verify(gson, times(0)).fromJson(givenJsonText, FileInfo.class);
    }


    @Test
    void getFileInfo_expectInvalidBotRequestEx_MultiVideoSpecifyNumberIsTooHigh() {
        //given
        String[] givenCommands = {"echo", "first\nsecond\nthird"};
        String givenURL = "https://www.twitter.com/example__id";
        String givenJsonText = null;
        int givenMultiVideoNumber = 4;

        FileRequest givenRequest = FileRequest.builder()
                .URL(givenURL)
                .multiVideoNumber(givenMultiVideoNumber)
                .build();

        FileInfo givenFileInfo = FileInfo.builder().uploaderURL("twitter.com/").build();

        //when
        when(commands.fileInfoTwitter(givenURL)).thenReturn(givenCommands);
        when(gson.fromJson(any(String.class), eq(FileInfo.class))).thenReturn(givenFileInfo);

        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
        verify(commands).fileInfoTwitter(givenURL);
    }


    @Test
    void getFileInfo_expectInvalidBotRequestEx_LiveVideo() {
        //given
        String[] givenCommands = {"echo", "first"};
        String givenURL = "https://www.twitter.com/example__id";
        String givenJsonText = "first";
        int givenMultiVideoNumber = 1;

        FileRequest givenRequest = FileRequest.builder()
                .URL(givenURL)
                .multiVideoNumber(givenMultiVideoNumber)
                .build();

        FileInfo givenFileInfo = FileInfo.builder()
                .uploaderURL("twitter.com/")
                .isLive(true)
                .build();

        //when
        when(commands.fileInfoTwitter(givenURL)).thenReturn(givenCommands);
        when(gson.fromJson(givenJsonText, FileInfo.class)).thenReturn(givenFileInfo);

        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
        verify(commands).fileInfoTwitter(givenURL);
        verify(gson, times(2)).fromJson(givenJsonText, FileInfo.class);
    }


    @Test
    void getFileFromRepository_expectEquals_ValidFileResponse() {
        //given
        String givenId = "example_id";
        String givenExtension = "mp3";
        String givenSize = "10MB";
        String givenFileId = "fileId";
        String givenQuality = "premium";
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        TwitterFileEntity fileEntity = TwitterFileEntity.builder()
                .size(givenSize)
                .fileId(givenFileId)
                .twitterId(givenId)
                .quality(givenQuality)
                .extension(givenExtension)
                .build();

        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .extension(givenExtension)
                .premium(true)
                .size(10000000L)
                .downloadedFile(InputFile.builder().fileId(givenFileId).build())
                .build();
        //when
        when(fileService.findByTwitterIdAndExtensionAndQuality(givenId, givenExtension, givenQuality)).thenReturn(Optional.ofNullable(fileEntity));
        when(converter.MBToBytes(givenSize)).thenReturn(10000000L);

        FileResponse actualFileResponse = downloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileService).findByTwitterIdAndExtensionAndQuality(givenId, givenExtension, givenQuality);
        verify(converter, times(2)).MBToBytes(givenSize);
    }


    @Test
    void getFileFromRepository_expectEquals_SizeFileInRepositoryOver20MB() {
        //given
        String givenId = "example_id";
        String givenExtension = "mp3";
        String givenSize = "30MB";
        String givenFileId = "fileId";
        String givenQuality = "premium";
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        TwitterFileEntity fileEntity = TwitterFileEntity.builder()
                .size(givenSize)
                .fileId(givenFileId)
                .twitterId(givenId)
                .quality(givenQuality)
                .extension(givenExtension)
                .build();

        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        //when
        when(fileService.findByTwitterIdAndExtensionAndQuality(givenId, givenExtension, givenQuality)).thenReturn(Optional.ofNullable(fileEntity));
        when(converter.MBToBytes(givenSize)).thenReturn(30000000L);

        FileResponse actualFileResponse = downloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileService).findByTwitterIdAndExtensionAndQuality(givenId, givenExtension, givenQuality);

        verify(converter, atMostOnce()).MBToBytes(givenSize);
    }


    @Test
    void getFileFromRepository_expectEquals_FileEntityDoesNotExistsInRepository() {
        //given
        String givenId = "example_id";
        String givenExtension = "mp3";
        String givenQuality = "standard";
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        TwitterFileEntity fileEntity = null;

        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        //when
        when(fileService.findByTwitterIdAndExtensionAndQuality(givenId, givenExtension, givenQuality)).thenReturn(Optional.ofNullable(fileEntity));

        FileResponse actualFileResponse = downloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileService, atMostOnce()).findByTwitterIdAndExtensionAndQuality(givenId, givenExtension, givenQuality);
    }
}
