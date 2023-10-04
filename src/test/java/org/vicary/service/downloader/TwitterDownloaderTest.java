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
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(Runner.class)
@SpringBootTest
class TwitterDownloaderTest {
    @Autowired
    private TwitterDownloader downloader;

    @MockBean
    private TwitterFileService fileService;

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
    private DownloaderManager downloaderManager;

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
        when(mapper.map(givenFileInfo)).thenReturn(givenFileResponse);

        FileResponse actualFileResponse = downloader.getFileInfo(givenRequest, processBuilder);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(commands).fileInfoTwitter(givenURL);
        verify(gson, times(2)).fromJson(givenJsonText, FileInfo.class);
        verify(mapper).map(givenFileInfo);
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
        String[] givenCommands = {"echo", "first\nsecond\nthird\nfourth\nfifth\nsixth\nseventh\neighth\nninth\ntenth\neleventh" +
                                          "\ntwelfth\nthirteenth\nfourteenth\nfifteenth\nsixteenth"};
        String givenURL = "https://www.twitter.com/example__id";

        int givenMultiVideoNumber = 18;

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
        when(fileService.findByTwitterId(givenFileResponse.getServiceId())).thenReturn(Optional.ofNullable(fileEntity));
        when(converter.MBToBytes(givenSize)).thenReturn(10000000L);

        FileResponse actualFileResponse = downloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileService).findByTwitterId(givenId);
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
        when(fileService.findByTwitterId(givenId)).thenReturn(Optional.ofNullable(fileEntity));
        when(converter.MBToBytes(givenSize)).thenReturn(30000000L);

        FileResponse actualFileResponse = downloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileService).findByTwitterId(givenId);
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
        TwitterFileEntity fileEntity = null;

        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .extension(givenExtension)
                .premium(true)
                .build();
        //when
        when(fileService.findByTwitterId(givenId)).thenReturn(Optional.ofNullable(fileEntity));

        FileResponse actualFileResponse = downloader.getFileFromRepository(givenFileResponse);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(fileService).findByTwitterId(givenId);
    }


    @Test
    void downloadFile_expectEquals_ValidFileResponse() throws IOException {
        //given
        String[] givenCommand = {"echo", "nothing"};
        String givenId = "example_id";
        String givenTitle = "title";
        String givenExtension = "mp3";
        String givenURL = "https://www.twitter.com/example_id";
        int givenMultiVideoNumber = 1;
        EditMessageText editMessageText = new EditMessageText("chatId", 123, "text");
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .URL(givenURL)
                .title(givenTitle)
                .editMessageText(editMessageText)
                .extension(givenExtension)
                .multiVideoNumber(givenMultiVideoNumber)
                .premium(true)
                .build();


        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .URL(givenURL)
                .title(givenTitle)
                .editMessageText(editMessageText)
                .extension(givenExtension)
                .premium(true)
                .size(11688889)
                .multiVideoNumber(givenMultiVideoNumber)
                .downloadedFile(new InputFile(null, FILE_MP3_VALID, false))
                .build();

        //when
        when(downloaderManager.getFileNameFromTitle(givenTitle, givenExtension)).thenReturn(FILE_MP3_VALID.getName());
        when(commands.getDownloadDestination()).thenReturn(DESTINATION);
        when(commands.downloadTwitter(FILE_MP3_VALID.getName(), givenURL, givenMultiVideoNumber)).thenReturn(givenCommand);
        when(downloaderManager.isFileSizeValid(FILE_MP3_VALID.length())).thenReturn(true);

        FileResponse actualFileResponse = downloader.downloadFile(givenFileResponse, processBuilder);

        //then
        assertEquals(expectedFileResponse, actualFileResponse);
        verify(downloaderManager).getFileNameFromTitle(givenTitle, givenExtension);
        verify(commands).getDownloadDestination();
        verify(commands).downloadTwitter(FILE_MP3_VALID.getName(), givenURL, givenMultiVideoNumber);
        verify(downloaderManager).isFileSizeValid(FILE_MP3_VALID.length());
    }


    @Test
    void downloadFile_expectThrowsInvalidBotRequestEx_DownloadingFileOver50MB() {
        //given
        String[] givenCommand = {"echo", "[download] File is larger than max-filesize(38248888 bytes...)"};
        String givenId = "example_id";
        String givenTitle = "title";
        String givenExtension = "mp3";
        String givenFileName = "fileName";
        String givenURL = "https://www.twitter.com/example_id";
        int givenMultiVideoNumber = 1;
        EditMessageText editMessageText = new EditMessageText("chatId", 123, "text");
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .URL(givenURL)
                .title(givenTitle)
                .editMessageText(editMessageText)
                .extension(givenExtension)
                .multiVideoNumber(givenMultiVideoNumber)
                .premium(true)
                .build();

        //when
        when(downloaderManager.getFileNameFromTitle(givenTitle, givenExtension)).thenReturn(givenFileName);
        when(commands.downloadTwitter(givenFileName, givenURL, givenMultiVideoNumber)).thenReturn(givenCommand);
        when(downloaderManager.isFileDownloadingInProcess(givenCommand[1])).thenReturn(true);
        when(downloaderManager.isFileSizeValidInProcess(givenCommand[1])).thenReturn(false);


        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.downloadFile(givenFileResponse, processBuilder));
        verify(downloaderManager).getFileNameFromTitle(givenTitle, givenExtension);
        verify(downloaderManager).isFileDownloadedInProcess(givenCommand[1]);
    }


    @Test
    void downloadFile_expectThrowsInvalidBotRequestEx_DownloadedFileOver50MB() {
        //given
        String[] givenCommand = {"echo", "nothing"};
        String givenId = "example_id";
        String givenTitle = "title";
        String givenExtension = "mp4";
        String givenURL = "https://www.twitter.com/example_id";
        int givenMultiVideoNumber = 1;
        EditMessageText editMessageText = new EditMessageText("chatId", 123, "text");
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .URL(givenURL)
                .title(givenTitle)
                .editMessageText(editMessageText)
                .extension(givenExtension)
                .multiVideoNumber(givenMultiVideoNumber)
                .premium(true)
                .build();


        //when
        when(downloaderManager.getFileNameFromTitle(givenTitle, givenExtension)).thenReturn(FILE_MP4_OVER_50MB.getName());
        when(commands.getDownloadDestination()).thenReturn(DESTINATION);
        when(commands.downloadTwitter(FILE_MP4_OVER_50MB.getName(), givenURL, givenMultiVideoNumber)).thenReturn(givenCommand);
        when(downloaderManager.isFileSizeValid(FILE_MP4_OVER_50MB.length())).thenReturn(false);


        //then
        assertThrows(InvalidBotRequestException.class, () -> downloader.downloadFile(givenFileResponse, processBuilder));
        verify(downloaderManager).getFileNameFromTitle(givenTitle, givenExtension);
        verify(commands).getDownloadDestination();
        verify(commands).downloadTwitter(FILE_MP4_OVER_50MB.getName(), givenURL, givenMultiVideoNumber);
        verify(downloaderManager).isFileSizeValid(FILE_MP4_OVER_50MB.length());
    }


    @Test
    void downloadFile_expectThrowsDownloadedFileNotFoundEx_DownloadedFileDoesNotExist() {
        //given
        String[] givenCommand = {"echo", "nothing"};
        String givenId = "example_id";
        String givenTitle = "title";
        String givenExtension = "mp4";
        String givenURL = "https://www.twitter.com/example_id";
        int givenMultiVideoNumber = 1;
        EditMessageText editMessageText = new EditMessageText("chatId", 123, "text");
        FileResponse givenFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .URL(givenURL)
                .title(givenTitle)
                .editMessageText(editMessageText)
                .extension(givenExtension)
                .multiVideoNumber(givenMultiVideoNumber)
                .premium(true)
                .build();


        //when
        when(downloaderManager.getFileNameFromTitle(givenTitle, givenExtension)).thenReturn(FILE_NOT_EXIST.getName());
        when(commands.getDownloadDestination()).thenReturn(DESTINATION);
        when(commands.downloadTwitter(FILE_NOT_EXIST.getName(), givenURL, givenMultiVideoNumber)).thenReturn(givenCommand);


        //then
        assertThrows(DownloadedFileNotFoundException.class, () -> downloader.downloadFile(givenFileResponse, processBuilder));
        verify(downloaderManager).getFileNameFromTitle(givenTitle, givenExtension);
        verify(commands).getDownloadDestination();
        verify(commands).downloadTwitter(FILE_NOT_EXIST.getName(), givenURL, givenMultiVideoNumber);
    }
}
