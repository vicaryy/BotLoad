package org.vicary.service.downloader;

import com.google.gson.Gson;
import org.aspectj.util.FileUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.command.YtDlpCommand;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.exception.DownloadedFileNotFoundException;
import org.vicary.exception.InvalidBotRequestException;
import org.vicary.info.DownloaderInfo;
import org.vicary.model.FileInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.pattern.Pattern;
import org.vicary.service.Converter;
import org.vicary.service.file_service.YouTubeFileService;
import org.vicary.service.mapper.FileInfoMapper;
import org.vicary.service.quick_sender.QuickSender;

import java.io.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class YouTubeDownloaderTest {

    @Autowired
    private YouTubeDownloader downloader;

    @MockBean
    private YouTubeFileService fileService;

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
    private static File FILE_THUMBNAIL;
    private static File FILE_NOT_EXIST;
    private static File FILE_NOT_EXIST_THUMBNAIL;
    private static File FILE_OVER_50MB;

    @BeforeAll
    public static void beforeAll() throws IOException {
        FILE_NORMAL = new File("file_normal.mp3");
        byte[] bytesForNormalFile = new byte[1000000];
        try (OutputStream outputStream = new FileOutputStream(FILE_NORMAL)) {
            outputStream.write(bytesForNormalFile);
        }

        FILE_THUMBNAIL = new File("file_thumbnail.jpg");
        byte[] bytesForThumbnailFile = new byte[1000000];
        try (OutputStream outputStream = new FileOutputStream(FILE_THUMBNAIL)) {
            outputStream.write(bytesForThumbnailFile);
        }

        FILE_OVER_50MB = new File("file_over_50MB.mp3");
        byte[] bytesForOver50MBFile = new byte[55000000];
        try (OutputStream outputStream = new FileOutputStream(FILE_OVER_50MB)) {
            outputStream.write(bytesForOver50MBFile);
        }

        FILE_NOT_EXIST = new File("file_not_exist.mp3");

        FILE_NOT_EXIST_THUMBNAIL = new File("file_not_exist_thumbnail.jpg");
    }

    @AfterAll
    public static void afterAll() {
        FileUtil.deleteContents(FILE_NORMAL);
        FileUtil.deleteContents(FILE_THUMBNAIL);
        FileUtil.deleteContents(FILE_OVER_50MB);
    }

    @Test
    void downloadThumbnail_expectEquals_NormalFileResponse() throws IOException {
        //given
        String[] givenCommand = {"echo", "hello"};
        EditMessageText givenEMT = new EditMessageText("chatId", 123, "text");
        String givenServiceId = "12345";
        String givenTitle = "file_thumbnail";
        String givenFileDestination = "";
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenServiceId)
                .title(givenTitle)
                .editMessageText(givenEMT)
                .build();

        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenServiceId)
                .title(givenTitle)
                .editMessageText(givenEMT)
                .thumbnail(InputFile.builder()
                        .file(FILE_THUMBNAIL)
                        .isThumbnail(true)
                        .build())
                .build();

        //when
        when(commands.downloadThumbnailYoutube(givenTitle + ".jpg", givenServiceId)).thenReturn(givenCommand);
        when(commands.getDownloadDestination()).thenReturn(givenFileDestination);
        when(downloaderManager.isFileDownloadingInProcess(givenCommand[1])).thenReturn(true);

        FileResponse actualFileResponse = downloader.downloadThumbnail(givenFileResponse, processBuilder);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(downloaderManager, times(1)).isFileDownloadingInProcess(givenCommand[1]);
        verify(downloaderManager, times(1)).updateDownloadProgressInEditMessageText(givenEMT, givenCommand[1]);
    }


    @Test
    void downloadThumbnail_expectEquals_FileDoesNotDownloaded() throws IOException {
        //given
        String[] givenCommand = {"echo", "hello"};
        EditMessageText givenEMT = new EditMessageText("chatId", 123, "text");
        String givenServiceId = "12345";
        String givenTitle = "file_not_exist_thumbnail";
        String givenFileDestination = "";
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenServiceId)
                .title(givenTitle)
                .editMessageText(givenEMT)
                .build();

        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenServiceId)
                .title(givenTitle)
                .editMessageText(givenEMT)
                .thumbnail(InputFile.builder()
                        .file(FILE_NOT_EXIST_THUMBNAIL)
                        .isThumbnail(true)
                        .build())
                .build();

        //when
        when(commands.downloadThumbnailYoutube(givenTitle + ".jpg", givenServiceId)).thenReturn(givenCommand);
        when(commands.getDownloadDestination()).thenReturn(givenFileDestination);
        when(downloaderManager.isFileDownloadingInProcess(givenCommand[1])).thenReturn(false);

        FileResponse actualFileResponse = downloader.downloadThumbnail(givenFileResponse, processBuilder);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(downloaderManager, times(1)).isFileDownloadingInProcess(givenCommand[1]);
        verify(downloaderManager, never()).updateDownloadProgressInEditMessageText(givenEMT, givenCommand[1]);
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
        when(commands.downloadYouTube(givenFilePath, givenFileResponse)).thenReturn(givenCommand);
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
        when(commands.downloadYouTube(givenFilePath, givenFileResponse)).thenReturn(givenCommand);
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
        when(commands.downloadYouTube(givenFilePath, givenFileResponse)).thenReturn(givenCommand);
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
        when(commands.downloadYouTube(givenFilePath, givenFileResponse)).thenReturn(givenCommand);
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
        YouTubeFileEntity fileEntity = YouTubeFileEntity.builder()
                .size(givenSize)
                .fileId(givenFileId)
                .youtubeId(givenId)
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
        when(fileService.findByYoutubeIdAndExtensionAndQuality(
                givenFileResponse.getServiceId(),
                givenFileResponse.getExtension(),
                givenFileResponse.isPremium() ? "premium" : "standard"))
                .thenReturn(Optional.ofNullable(fileEntity));
        when(converter.MBToBytes(givenSize)).thenReturn(10000000L);

        FileResponse actualFileResponse = downloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileService).findByYoutubeIdAndExtensionAndQuality(
                givenFileResponse.getServiceId(),
                givenFileResponse.getExtension(),
                givenFileResponse.isPremium() ? "premium" : "standard");
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
        YouTubeFileEntity fileEntity = YouTubeFileEntity.builder()
                .size(givenSize)
                .fileId(givenFileId)
                .youtubeId(givenId)
                .quality(givenQuality)
                .extension(givenExtension)
                .build();

        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        //when
        when(fileService.findByYoutubeIdAndExtensionAndQuality(
                givenFileResponse.getServiceId(),
                givenFileResponse.getExtension(),
                givenFileResponse.isPremium() ? "premium" : "standard"))
                .thenReturn(Optional.ofNullable(fileEntity));
        when(converter.MBToBytes(givenSize)).thenReturn(30000000L);

        FileResponse actualFileResponse = downloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileService).findByYoutubeIdAndExtensionAndQuality(
                givenFileResponse.getServiceId(),
                givenFileResponse.getExtension(),
                givenFileResponse.isPremium() ? "premium" : "standard");
        verify(converter, times(1)).MBToBytes(givenSize);
    }

    @Test
    void getFileFromRepository_expectEquals_FileEntityDoesNotExistsInRepository() {
        //given
        String givenId = "example_id";
        String givenExtension = "mp3";
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        YouTubeFileEntity fileEntity = null;

        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        //when
        when(fileService.findByYoutubeIdAndExtensionAndQuality(
                givenFileResponse.getServiceId(),
                givenFileResponse.getExtension(),
                givenFileResponse.isPremium() ? "premium" : "standard"))
                .thenReturn(Optional.ofNullable(fileEntity));

        FileResponse actualFileResponse = downloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileService).findByYoutubeIdAndExtensionAndQuality(
                givenFileResponse.getServiceId(),
                givenFileResponse.getExtension(),
                givenFileResponse.isPremium() ? "premium" : "standard");
    }


    @Test
    void getFileInfo_expectEquals_ValidRequest() throws IOException {
        //given
        String[] givenCommands = {"echo", "nothing"};
        String givenId = "example_id";
        String givenTitle = "title";
        String givenExtension = "mp3";
        int givenDuration = 10;
        String givenURL = "https://www.youtube.com/example__id";
        String givenChatId = "chatId";
        String givenJsonText = "nothing";
        String givenServiceName = "youtube";
        FileInfo givenFileInfo = FileInfo.builder()
                .id(givenId)
                .title(givenTitle)
                .duration(givenDuration)
                .URL(givenURL)
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
                .editMessageText(editMessageText)
                .build();
        //when
        when(pattern.getYoutubeId(givenRequest.getURL())).thenReturn(givenId);
        when(commands.fileInfoYouTube(givenId)).thenReturn(givenCommands);
        when(gson.fromJson(givenJsonText, FileInfo.class)).thenReturn(givenFileInfo);
        when(mapper.map(givenFileInfo, givenServiceName)).thenReturn(givenFileResponse);

        FileResponse actualFileResponse = downloader.getFileInfo(givenRequest, processBuilder);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(pattern).getYoutubeId(givenRequest.getURL());
        verify(commands).fileInfoYouTube(givenId);
        verify(mapper).map(givenFileInfo, givenServiceName);
    }


    @Test
    void getFileInfo_expectThrow_InvalidYouTubeID() throws IOException {
        //given
        String[] givenCommands = {"echo", "nothing"};
        FileInfo givenFileInfo = null;
        EditMessageText editMessageText = new EditMessageText("chatId", 123, "text");
        FileRequest givenRequest = FileRequest.builder()
                .URL("https://www.youtube.com/invalid_id")
                .chatId("chatId")
                .extension("mp3")
                .premium(false)
                .editMessageText(editMessageText)
                .build();

        //when
        when(pattern.getYoutubeId(givenRequest.getURL())).thenReturn("invalid_id");
        when(commands.fileInfoYouTube("invalid_id")).thenReturn(givenCommands);
        when(gson.fromJson("nothing", FileInfo.class)).thenReturn(givenFileInfo);


        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
        verify(pattern).getYoutubeId(givenRequest.getURL());
        verify(commands).fileInfoYouTube("invalid_id");
    }


    @Test
    void getFileInfo_expectThrow_LiveVideo() throws IOException {
        //given
        String[] givenCommands = {"echo", "nothing"};
        String givenId = "example_id";
        String givenTitle = "title";
        String givenExtension = "mp3";
        double givenDuration = 10;
        String givenURL = "https://www.youtube.com/example__id";
        String givenChatId = "chatId";
        String givenJsonText = "nothing";
        FileInfo givenFileInfo = FileInfo.builder()
                .id(givenId)
                .title(givenTitle)
                .duration(givenDuration)
                .URL(givenURL)
                .isLive(true)
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
        when(pattern.getYoutubeId(givenRequest.getURL())).thenReturn(givenId);
        when(commands.fileInfoYouTube(givenId)).thenReturn(givenCommands);
        when(gson.fromJson(givenJsonText, FileInfo.class)).thenReturn(givenFileInfo);


        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenRequest, processBuilder));
        verify(pattern).getYoutubeId(givenRequest.getURL());
        verify(commands).fileInfoYouTube(givenId);
    }
}



















