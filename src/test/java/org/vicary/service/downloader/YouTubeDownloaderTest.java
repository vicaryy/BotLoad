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
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.exception.InvalidBotRequestException;
import org.vicary.info.DownloaderInfo;
import org.vicary.model.FileInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.pattern.Pattern;
import org.vicary.service.Converter;
import org.vicary.service.FileManager;
import org.vicary.service.file_service.YouTubeFileService;
import org.vicary.service.mapper.FileInfoMapper;
import org.vicary.service.quick_sender.QuickSender;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(Runner.class)
@SpringBootTest
class YouTubeDownloaderTest {

    @Autowired
    private YouTubeDownloader youTubeDownloader;

    @MockBean
    YouTubeFileService youTubeFileService;

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

    private static File VALID_FILE_IN_MP3;


    @BeforeAll
    public static void beforeAll() throws Exception {
        processBuilder.directory(new File(DESTINATION));
        VALID_FILE_IN_MP3 = new File("/Users/vicary/desktop/botTestFolder/validFile.mp3");
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
        FileInfo givenFileInfo = FileInfo.builder()
                .id(givenId)
                .title(givenTitle)
                .duration(givenDuration)
                .URL(givenURL)
                .build();
        FileResponse givenFileResponse = FileResponse.builder()
                .id(givenId)
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
                .id(givenId)
                .URL(givenURL)
                .duration(givenDuration)
                .title(givenTitle)
                .extension(givenExtension)
                .premium(false)
                .editMessageText(editMessageText)
                .build();
        //when
        when(pattern.getYoutubeId(givenRequest.getURL())).thenReturn(givenId);
        when(commands.getDownloadYouTubeFileInfo(givenId)).thenReturn(givenCommands);
        when(gson.fromJson(givenJsonText, FileInfo.class)).thenReturn(givenFileInfo);
        when(mapper.map(givenFileInfo)).thenReturn(givenFileResponse);

        FileResponse actualFileResponse = youTubeDownloader.getFileInfo(givenRequest, processBuilder);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(pattern).getYoutubeId(givenRequest.getURL());
        verify(commands).getDownloadYouTubeFileInfo(givenId);
        verify(mapper).map(givenFileInfo);
    }


    @Test
    void getFileInfo_expectThrowsInvalidBotRequest_InvalidYouTubeID() throws IOException {
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
        when(commands.getDownloadYouTubeFileInfo("invalid_id")).thenReturn(givenCommands);
        when(gson.fromJson("nothing", FileInfo.class)).thenReturn(givenFileInfo);


        //then
        assertThrows(InvalidBotRequestException.class, () -> youTubeDownloader.getFileInfo(givenRequest, processBuilder));
        verify(pattern).getYoutubeId(givenRequest.getURL());
        verify(commands).getDownloadYouTubeFileInfo("invalid_id");
    }


    @Test
    void getFileInfo_expectThrowsInvalidBotRequest_LiveVideo() throws IOException {
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
        when(commands.getDownloadYouTubeFileInfo(givenId)).thenReturn(givenCommands);
        when(gson.fromJson(givenJsonText, FileInfo.class)).thenReturn(givenFileInfo);


        //then
        assertThrows(InvalidBotRequestException.class, () -> youTubeDownloader.getFileInfo(givenRequest, processBuilder));
        verify(pattern).getYoutubeId(givenRequest.getURL());
        verify(commands).getDownloadYouTubeFileInfo(givenId);
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
        YouTubeFileEntity fileEntity = YouTubeFileEntity.builder()
                .size(givenSize)
                .fileId(givenFileId)
                .youtubeId(givenId)
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
        when(youTubeFileService.findByYoutubeIdAndExtensionAndQuality(
                givenFileResponse.getId(),
                givenFileResponse.getExtension(),
                givenFileResponse.isPremium() ? "premium" : "standard"))
                .thenReturn(Optional.ofNullable(fileEntity));
        when(converter.MBToBytes(givenSize)).thenReturn(10000000L);

        FileResponse actualFileResponse = youTubeDownloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(youTubeFileService).findByYoutubeIdAndExtensionAndQuality(
                givenFileResponse.getId(),
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
                .id(givenId)
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
                .id(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        //when
        when(youTubeFileService.findByYoutubeIdAndExtensionAndQuality(
                givenFileResponse.getId(),
                givenFileResponse.getExtension(),
                givenFileResponse.isPremium() ? "premium" : "standard"))
                .thenReturn(Optional.ofNullable(fileEntity));
        when(converter.MBToBytes(givenSize)).thenReturn(30000000L);

        FileResponse actualFileResponse = youTubeDownloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(youTubeFileService).findByYoutubeIdAndExtensionAndQuality(
                givenFileResponse.getId(),
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
                .id(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        YouTubeFileEntity fileEntity = null;

        FileResponse expectedFileResponse = FileResponse.builder()
                .id(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        //when
        when(youTubeFileService.findByYoutubeIdAndExtensionAndQuality(
                givenFileResponse.getId(),
                givenFileResponse.getExtension(),
                givenFileResponse.isPremium() ? "premium" : "standard"))
                .thenReturn(Optional.ofNullable(fileEntity));

        FileResponse actualFileResponse = youTubeDownloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(youTubeFileService).findByYoutubeIdAndExtensionAndQuality(
                givenFileResponse.getId(),
                givenFileResponse.getExtension(),
                givenFileResponse.isPremium() ? "premium" : "standard");
    }

    @Test
    void downloadFile_expectEquals_ValidFileResponse() throws IOException {
        //given
        String[] givenCommand = {"echo", "nothing"};
        String givenId = "example_id";
        String givenTitle = "title";
        String givenExtension = "mp3";
        EditMessageText editMessageText = new EditMessageText("chatId", 123, "text");
        FileResponse givenFileResponse = FileResponse.builder()
                .id(givenId)
                .title(givenTitle)
                .editMessageText(editMessageText)
                .extension(givenExtension)
                .premium(true)
                .build();


        FileResponse expectedFileResponse = FileResponse.builder()
                .id(givenId)
                .title(givenTitle)
                .editMessageText(editMessageText)
                .extension(givenExtension)
                .premium(true)
                .size(11688889)
                .downloadedFile(new InputFile(null, VALID_FILE_IN_MP3, false))
                .build();

        //when
        when(fileManager.getFileNameFromTitle(givenTitle, givenExtension)).thenReturn(VALID_FILE_IN_MP3.getName());
        when(commands.getDownloadDestination()).thenReturn(DESTINATION);
        when(commands.getDownloadYouTubeFile(VALID_FILE_IN_MP3.getName(), givenId, givenExtension, true)).thenReturn(givenCommand);
        when(fileManager.isFileSizeValid(VALID_FILE_IN_MP3.length())).thenReturn(true);

        FileResponse actualFileResponse = youTubeDownloader.downloadFile(givenFileResponse, processBuilder);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileManager).getFileNameFromTitle(givenTitle, givenExtension);
        verify(commands).getDownloadDestination();
        verify(commands).getDownloadYouTubeFile(VALID_FILE_IN_MP3.getName(), givenId, givenExtension, true);
        verify(fileManager).isFileSizeValid(VALID_FILE_IN_MP3.length());
    }
}














































