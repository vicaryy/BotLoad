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
import org.vicary.entity.TikTokFileEntity;
import org.vicary.exception.DownloadedFileNotFoundException;
import org.vicary.exception.InvalidBotRequestException;
import org.vicary.info.DownloaderInfo;
import org.vicary.model.FileInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.service.Converter;
import org.vicary.service.file_service.TikTokFileService;
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
class TikTokDownloaderTest {

    @Autowired
    private TikTokDownloader downloader;

    @MockBean
    private TikTokFileService fileService;

    @MockBean
    private FileInfoMapper mapper;

    @MockBean
    private DownloaderInfo info;

    @MockBean
    private QuickSender quickSender;

    @MockBean
    private Gson gson;

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
        when(commands.downloadTikTok(givenFilePath, givenFileResponse)).thenReturn(givenCommand);
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
        when(commands.downloadTikTok(givenFilePath, givenFileResponse)).thenReturn(givenCommand);
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
        when(commands.downloadTikTok(givenFilePath, givenFileResponse)).thenReturn(givenCommand);
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
        when(commands.downloadTikTok(givenFilePath, givenFileResponse)).thenReturn(givenCommand);
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
    void getFileInfo_expectEquals_ValidFileRequest() throws IOException {
        //given
        String[] givenCommand = {"echo", "nothing"};
        String givenChatId = "chatId";
        String givenURL = "https://tiktok.com/example_id";
        String givenExtension = "mp4";
        String givenTitle = "title";
        double givenDuration = 10;
        String givenId = "example_id";
        String givenServiceName = "tiktok";
        EditMessageText editMessageText = new EditMessageText(givenChatId, 999, "text");
        FileRequest givenFileRequest = FileRequest.builder()
                .URL(givenURL)
                .extension(givenExtension)
                .premium(true)
                .editMessageText(editMessageText)
                .build();
        FileInfo givenFileInfo = FileInfo.builder()
                .title(givenTitle)
                .duration(givenDuration)
                .uploaderURL("tiktok.com/")
                .id(givenId)
                .build();
        FileResponse givenFileResponse = FileResponse.builder()
                .title(givenTitle)
                .duration((int) givenDuration)
                .serviceId(givenId)
                .URL(givenURL)
                .build();

        FileResponse expectedFileResponse = FileResponse.builder()
                .URL(givenURL)
                .extension(givenExtension)
                .premium(true)
                .title(givenTitle)
                .duration((int) givenDuration)
                .serviceId(givenId)
                .editMessageText(editMessageText)
                .build();

        //when
        when(commands.fileInfoTikTok(givenURL)).thenReturn(givenCommand);
        when(gson.fromJson(givenCommand[1], FileInfo.class)).thenReturn(givenFileInfo);
        when(mapper.map(givenFileInfo, givenServiceName)).thenReturn(givenFileResponse);

        FileResponse actualFileResponse = downloader.getFileInfo(givenFileRequest, processBuilder);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(commands).fileInfoTikTok(givenURL);
        verify(gson).fromJson(givenCommand[1], FileInfo.class);
        verify(mapper).map(givenFileInfo, givenServiceName);
    }


    @Test
    void getFileInfo_expectThrowsInvalidBotRequestEx_NoVideoInURL() {
        //given
        String[] givenCommand = {"ls", ">/dev/null"};
        String givenURL = "https://tiktok.com/example_id";
        String givenExtension = "mp4";
        FileRequest givenFileRequest = FileRequest.builder()
                .URL(givenURL)
                .extension(givenExtension)
                .premium(true)
                .build();
        FileInfo givenFileInfo = null;

        //when
        when(commands.fileInfoTikTok(givenURL)).thenReturn(givenCommand);
        when(gson.fromJson((String) null, FileInfo.class)).thenReturn(givenFileInfo);

        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenFileRequest, processBuilder));
        verify(commands).fileInfoTikTok(givenURL);
    }


    @Test
    void getFileInfo_expectThrowsInvalidBotRequestEx_UploadedURLIsNull() {
        //given
        String[] givenCommand = {"echo", "nothing"};
        String givenURL = "https://tiktok.com/example_id";
        String givenExtension = "mp4";
        FileRequest givenFileRequest = FileRequest.builder()
                .URL(givenURL)
                .extension(givenExtension)
                .premium(true)
                .build();
        FileInfo givenFileInfo = FileInfo.builder()
                .uploaderURL(null)
                .build();

        //when
        when(commands.fileInfoTikTok(givenURL)).thenReturn(givenCommand);
        when(gson.fromJson(givenCommand[1], FileInfo.class)).thenReturn(givenFileInfo);

        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenFileRequest, processBuilder));
        verify(commands).fileInfoTikTok(givenURL);
        verify(gson).fromJson(givenCommand[1], FileInfo.class);
    }


    @Test
    void getFileInfo_expectThrowsInvalidBotRequestEx_UploadedURLIsOtherService() {
        //given
        String[] givenCommand = {"echo", "nothing"};
        String givenURL = "https://tiktok.com/example_id";
        String givenExtension = "mp4";
        FileRequest givenFileRequest = FileRequest.builder()
                .URL(givenURL)
                .extension(givenExtension)
                .premium(true)
                .build();
        FileInfo givenFileInfo = FileInfo.builder()
                .uploaderURL("youtube.com/:)")
                .build();

        //when
        when(commands.fileInfoTikTok(givenURL)).thenReturn(givenCommand);
        when(gson.fromJson(givenCommand[1], FileInfo.class)).thenReturn(givenFileInfo);

        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenFileRequest, processBuilder));
        verify(commands).fileInfoTikTok(givenURL);
        verify(gson).fromJson(givenCommand[1], FileInfo.class);
    }


    @Test
    void getFileInfo_expectThrowsInvalidBotRequestEx_LiveVideo() {
        //given
        String[] givenCommand = {"echo", "nothing"};
        String givenURL = "https://tiktok.com/example_id";
        String givenExtension = "mp4";
        FileRequest givenFileRequest = FileRequest.builder()
                .URL(givenURL)
                .extension(givenExtension)
                .premium(true)
                .build();
        FileInfo givenFileInfo = FileInfo.builder()
                .uploaderURL("tiktok.com/")
                .isLive(true)
                .build();

        //when
        when(commands.fileInfoTikTok(givenURL)).thenReturn(givenCommand);
        when(commands.fileInfoTikTok(givenURL)).thenReturn(givenCommand);
        when(gson.fromJson(givenCommand[1], FileInfo.class)).thenReturn(givenFileInfo);

        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenFileRequest, processBuilder));
        verify(commands).fileInfoTikTok(givenURL);
        verify(gson).fromJson(givenCommand[1], FileInfo.class);
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
        TikTokFileEntity fileEntity = TikTokFileEntity.builder()
                .size(givenSize)
                .fileId(givenFileId)
                .tiktokId(givenId)
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
        when(fileService.findByTiktokIdAndExtensionAndQuality(givenId, givenExtension, givenQuality)).thenReturn(Optional.ofNullable(fileEntity));
        when(converter.MBToBytes(givenSize)).thenReturn(10000000L);

        FileResponse actualFileResponse = downloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileService).findByTiktokIdAndExtensionAndQuality(givenId, givenExtension, givenQuality);
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
        TikTokFileEntity fileEntity = TikTokFileEntity.builder()
                .size(givenSize)
                .fileId(givenFileId)
                .tiktokId(givenId)
                .quality(givenQuality)
                .extension(givenExtension)
                .build();

        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        //when
        when(fileService.findByTiktokIdAndExtensionAndQuality(givenId, givenExtension, givenQuality)).thenReturn(Optional.ofNullable(fileEntity));
        when(converter.MBToBytes(givenSize)).thenReturn(30000000L);

        FileResponse actualFileResponse = downloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileService).findByTiktokIdAndExtensionAndQuality(givenId, givenExtension, givenQuality);
        verify(converter, times(1)).MBToBytes(givenSize);
    }


    @Test
    void getFileFromRepository_expectEquals_FileEntityDoesNotExistsInRepository() {
        //given
        String givenId = "example_id";
        String givenExtension = "mp3";
        String givenQuality = "premium";
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        TikTokFileEntity fileEntity = null;

        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        //when
        when(fileService.findByTiktokIdAndExtensionAndQuality(givenId, givenExtension, givenQuality)).thenReturn(Optional.ofNullable(fileEntity));

        FileResponse actualFileResponse = downloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileService).findByTiktokIdAndExtensionAndQuality(givenId, givenExtension, givenQuality);
    }


}
