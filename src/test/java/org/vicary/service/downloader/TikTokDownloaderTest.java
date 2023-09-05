package org.vicary.service.downloader;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
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
import org.vicary.pattern.Pattern;
import org.vicary.service.Converter;
import org.vicary.service.FileManager;
import org.vicary.service.file_service.TikTokFileService;
import org.vicary.service.mapper.FileInfoMapper;
import org.vicary.service.quick_sender.QuickSender;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(Runner.class)
@SpringBootTest
class TikTokDownloaderTest {

    @Autowired
    private TikTokDownloader downloader;

    @MockBean
    private TikTokFileService fileService;

    @MockBean
    private DownloaderInfo info;

    @MockBean
    private FileInfoMapper mapper;

    @MockBean
    private YtDlpCommand commands;

    @MockBean
    private QuickSender quickSender;

    @MockBean
    private Gson gson;

    @MockBean
    private Pattern pattern;

    @MockBean
    private Converter converter;

    @MockBean
    private FileManager fileManager;

    private final static ProcessBuilder processBuilder = new ProcessBuilder();

    private final static String DESTINATION = "/Users/vicary/desktop/botTestFolder/";
    private static File FILE_MP3_VALID;
    private static File FILE_MP4_OVER_50MB;
    private static File FILE_NOT_EXIST;

    @BeforeAll
    public static void beforeAll() throws Exception {
        processBuilder.directory(new File(DESTINATION));
        FILE_MP3_VALID = new File("/Users/vicary/desktop/botTestFolder/validFile.mp3");
        FILE_MP4_OVER_50MB = new File("/Users/vicary/desktop/botTestFolder/fileOver50MB.mp4");
        FILE_NOT_EXIST = new File("/Users/vicary/desktop/botTestFolder/NO FILE");
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
        EditMessageText editMessageText = new EditMessageText(givenChatId, 111, "text");
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
                .id(givenId)
                .URL(givenURL)
                .build();

        FileResponse expectedFileResponse = FileResponse.builder()
                .URL(givenURL)
                .extension(givenExtension)
                .premium(true)
                .title(givenTitle)
                .duration((int) givenDuration)
                .id(givenId)
                .editMessageText(editMessageText)
                .build();

        //when
        when(commands.getDownloadFileInfo(givenURL)).thenReturn(givenCommand);
        when(gson.fromJson(givenCommand[1], FileInfo.class)).thenReturn(givenFileInfo);
        when(mapper.map(givenFileInfo)).thenReturn(givenFileResponse);

        FileResponse actualFileResponse = downloader.getFileInfo(givenFileRequest, processBuilder);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(commands).getDownloadFileInfo(givenURL);
        verify(gson).fromJson(givenCommand[1], FileInfo.class);
        verify(mapper).map(givenFileInfo);
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
        when(commands.getDownloadFileInfo(givenURL)).thenReturn(givenCommand);
        when(gson.fromJson((String) null, FileInfo.class)).thenReturn(givenFileInfo);

        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenFileRequest, processBuilder));
        verify(commands).getDownloadFileInfo(givenURL);
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
        when(commands.getDownloadFileInfo(givenURL)).thenReturn(givenCommand);
        when(gson.fromJson(givenCommand[1], FileInfo.class)).thenReturn(givenFileInfo);

        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenFileRequest, processBuilder));
        verify(commands).getDownloadFileInfo(givenURL);
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
                .uploaderURL("youtube.com/")
                .build();

        //when
        when(commands.getDownloadFileInfo(givenURL)).thenReturn(givenCommand);
        when(gson.fromJson(givenCommand[1], FileInfo.class)).thenReturn(givenFileInfo);

        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenFileRequest, processBuilder));
        verify(commands).getDownloadFileInfo(givenURL);
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
        when(commands.getDownloadFileInfo(givenURL)).thenReturn(givenCommand);
        when(gson.fromJson(givenCommand[1], FileInfo.class)).thenReturn(givenFileInfo);

        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.getFileInfo(givenFileRequest, processBuilder));
        verify(commands).getDownloadFileInfo(givenURL);
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
                .id(givenId)
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
                .id(givenId)
                .extension(givenExtension)
                .premium(true)
                .size(10000000L)
                .downloadedFile(InputFile.builder().fileId(givenFileId).build())
                .build();
        //when
        when(fileService.findByTikTokId(givenFileResponse.getId())).thenReturn(Optional.ofNullable(fileEntity));
        when(converter.MBToBytes(givenSize)).thenReturn(10000000L);

        FileResponse actualFileResponse = downloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileService).findByTikTokId(givenId);
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
                .id(givenId)
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
                .id(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        //when
        when(fileService.findByTikTokId(givenId)).thenReturn(Optional.ofNullable(fileEntity));
        when(converter.MBToBytes(givenSize)).thenReturn(30000000L);

        FileResponse actualFileResponse = downloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileService).findByTikTokId(givenId);
        verify(converter, times(1)).MBToBytes(givenSize);
    }


    @Test
    void getFileFromRepository_expectEquals_FileEntityDoesNotExistsInRepository() {
        //given
        String givenId = "example_id";
        String givenExtension = "mp3";
        FileResponse givenFileResponse = FileResponse.builder()
                .id(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        TikTokFileEntity fileEntity = null;

        FileResponse expectedFileResponse = FileResponse.builder()
                .id(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        //when
        when(fileService.findByTikTokId(givenId)).thenReturn(Optional.ofNullable(fileEntity));

        FileResponse actualFileResponse = downloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileService).findByTikTokId(givenId);
    }


    @Test
    void downloadFile_expectEquals_ValidFileResponse() throws IOException {
        //given
        String[] givenCommand = {"echo", "nothing"};
        String givenId = "example_id";
        String givenTitle = "title";
        String givenExtension = "mp3";
        String givenURL = "https://www.tiktok.com/example_id";
        EditMessageText editMessageText = new EditMessageText("chatId", 123, "text");
        FileResponse givenFileResponse = FileResponse.builder()
                .id(givenId)
                .URL(givenURL)
                .title(givenTitle)
                .editMessageText(editMessageText)
                .extension(givenExtension)
                .premium(true)
                .build();


        FileResponse expectedFileResponse = FileResponse.builder()
                .id(givenId)
                .URL(givenURL)
                .title(givenTitle)
                .editMessageText(editMessageText)
                .extension(givenExtension)
                .premium(true)
                .size(11688889)
                .downloadedFile(new InputFile(null, FILE_MP3_VALID, false))
                .build();

        //when
        when(fileManager.getFileNameFromTitle(givenTitle, givenExtension)).thenReturn(FILE_MP3_VALID.getName());
        when(commands.getDownloadDestination()).thenReturn(DESTINATION);
        when(commands.getDownloadTikTokFile(FILE_MP3_VALID.getName(), givenURL)).thenReturn(givenCommand);
        when(fileManager.isFileSizeValid(FILE_MP3_VALID.length())).thenReturn(true);

        FileResponse actualFileResponse = downloader.downloadFile(givenFileResponse, processBuilder);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileManager).getFileNameFromTitle(givenTitle, givenExtension);
        verify(commands).getDownloadDestination();
        verify(commands).getDownloadTikTokFile(FILE_MP3_VALID.getName(), givenURL);
        verify(fileManager).isFileSizeValid(FILE_MP3_VALID.length());
    }


    @Test
    void downloadFile_expectThrowsInvalidBotRequestEx_DownloadingFileOver50MB() {
        //given
        String[] givenCommand = {"echo", "[download] File is larger than max-filesize(38248888 bytes...)"};
        String givenId = "example_id";
        String givenTitle = "title";
        String givenExtension = "mp3";
        String givenFileName = "fileName";
        String givenURL = "https://www.tiktok.com/example_id";
        EditMessageText editMessageText = new EditMessageText("chatId", 123, "text");
        FileResponse givenFileResponse = FileResponse.builder()
                .id(givenId)
                .URL(givenURL)
                .title(givenTitle)
                .editMessageText(editMessageText)
                .extension(givenExtension)
                .build();

        //when
        when(fileManager.getFileNameFromTitle(givenTitle, givenExtension)).thenReturn(givenFileName);
        when(commands.getDownloadTikTokFile(givenFileName, givenURL)).thenReturn(givenCommand);
        when(fileManager.isFileDownloadingInProcess(givenCommand[1])).thenReturn(true);
        when(fileManager.isFileSizeValidInProcess(givenCommand[1])).thenReturn(false);


        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.downloadFile(givenFileResponse, processBuilder));
        verify(fileManager).getFileNameFromTitle(givenTitle, givenExtension);
        verify(fileManager).isFileDownloadedInProcess(givenCommand[1]);
    }


    @Test
    void downloadFile_expectThrowsInvalidBotRequestEx_DownloadedFileOver50MB() {
        //given
        String[] givenCommand = {"echo", "nothing"};
        String givenId = "example_id";
        String givenTitle = "title";
        String givenExtension = "mp4";
        String givenURL = "https://www.tiktok.com/example_id";
        EditMessageText editMessageText = new EditMessageText("chatId", 123, "text");
        FileResponse givenFileResponse = FileResponse.builder()
                .id(givenId)
                .URL(givenURL)
                .title(givenTitle)
                .editMessageText(editMessageText)
                .extension(givenExtension)
                .build();


        //when
        when(fileManager.getFileNameFromTitle(givenTitle, givenExtension)).thenReturn(FILE_MP4_OVER_50MB.getName());
        when(commands.getDownloadDestination()).thenReturn(DESTINATION);
        when(commands.getDownloadTikTokFile(FILE_MP4_OVER_50MB.getName(), givenURL)).thenReturn(givenCommand);
        when(fileManager.isFileSizeValid(FILE_MP4_OVER_50MB.length())).thenReturn(false);


        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.downloadFile(givenFileResponse, processBuilder));
        verify(fileManager).getFileNameFromTitle(givenTitle, givenExtension);
        verify(commands).getDownloadDestination();
        verify(commands).getDownloadTikTokFile(FILE_MP4_OVER_50MB.getName(), givenURL);
        verify(fileManager).isFileSizeValid(FILE_MP4_OVER_50MB.length());
    }


    @Test
    void downloadFile_expectThrowsDownloadedFileNotFoundEx_DownloadedFileDoesNotExist() {
        //given
        String[] givenCommand = {"echo", "nothing"};
        String givenId = "example_id";
        String givenTitle = "title";
        String givenExtension = "mp4";
        String givenURL = "https://www.tiktok.com/example_id";
        EditMessageText editMessageText = new EditMessageText("chatId", 123, "text");
        FileResponse givenFileResponse = FileResponse.builder()
                .id(givenId)
                .URL(givenURL)
                .title(givenTitle)
                .editMessageText(editMessageText)
                .extension(givenExtension)
                .premium(true)
                .build();


        //when
        when(fileManager.getFileNameFromTitle(givenTitle, givenExtension)).thenReturn(FILE_NOT_EXIST.getName());
        when(commands.getDownloadDestination()).thenReturn(DESTINATION);
        when(commands.getDownloadTikTokFile(FILE_NOT_EXIST.getName(), givenURL)).thenReturn(givenCommand);


        //then
        assertThrows(DownloadedFileNotFoundException.class, () -> downloader.downloadFile(givenFileResponse, processBuilder));
        verify(fileManager).getFileNameFromTitle(givenTitle, givenExtension);
        verify(commands).getDownloadDestination();
        verify(commands).getDownloadTikTokFile(FILE_NOT_EXIST.getName(), givenURL);
    }
}

































