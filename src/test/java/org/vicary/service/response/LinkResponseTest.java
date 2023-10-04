package org.vicary.service.response;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.vicary.api_object.Audio;
import org.vicary.api_object.message.Message;
import org.vicary.api_object.video.Video;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.api_request.send.SendAudio;
import org.vicary.api_request.send.SendVideo;
import org.vicary.info.ResponseInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.service.Converter;
import org.vicary.service.RequestService;
import org.vicary.service.TerminalExecutor;
import org.vicary.service.downloader.*;
import org.vicary.service.file_service.*;
import org.vicary.service.quick_sender.QuickSender;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(Runner.class)
@SpringBootTest
class LinkResponseTest {
    @Autowired
    private LinkResponse linkResponse;

    @Autowired
    private ResponseInfo info;

    @MockBean
    private YouTubeDownloader youtubeDownloader;
    @MockBean
    private TwitterDownloader twitterDownloader;
    @MockBean
    private TikTokDownloader tiktokDownloader;
    @MockBean
    private InstagramDownloader instagramDownloader;

    @MockBean
    private InstagramFileService instagramFileService;
    @MockBean
    private TikTokFileService tiktokFileService;
    @MockBean
    private TwitterFileService twitterFileService;
    @MockBean
    private YouTubeFileService youtubeFileService;

    @MockBean
    private RequestService requestService;

    @MockBean
    private QuickSender quickSender;

    @MockBean
    private Converter converter;

    @MockBean
    private TerminalExecutor terminalExecutor;

    @Test
    void sendFile_expectEquals_ValidYouTubeRequestInMp3() throws Exception {
        //given
        Downloader givenDownloader = youtubeDownloader;
        FileService givenFileService = youtubeFileService;
        FileRequest givenFileRequest = FileRequest.builder()
                .chatId("123")
                .extension("mp3")
                .editMessageText(new EditMessageText("123", 321, "text"))
                .build();

        int duration = 10;
        long size = 100000;
        FileResponse fileResponse = FileResponse.builder()
                .URL("https://www.youtube.com/example__id")
                .serviceId("example__id")
                .extension("mp3")
                .premium(false)
                .title("Title")
                .duration(duration)
                .size(size)
                .chatId("123")
                .multiVideoNumber(0)
                .downloadedFile(InputFile.builder().file(new File("/example")).build())
                .thumbnail(InputFile.builder().file(new File("/example/")).build())
                .build();
        SendAudio sendAudio = linkResponse.getSendAudio(fileResponse);
        Audio audio = Audio.builder().fileId("999").build();
        Message message = Message.builder().audio(audio).build();

        //when
        when(givenDownloader.download(givenFileRequest)).thenReturn(fileResponse);
        when(requestService.sendRequest(sendAudio)).thenReturn(message);
        when(givenDownloader.getServiceName()).thenReturn("youtube");
        when(converter.secondsToMinutes(duration)).thenReturn("0:10");
        when(converter.bytesToMB(size)).thenReturn("1,00MB");
        when(givenFileService.existsInRepo(fileResponse)).thenReturn(false);

        linkResponse.response(givenFileRequest, givenDownloader, givenFileService);

        //then
        verify(givenDownloader).download(givenFileRequest);
        verify(requestService).sendRequest(sendAudio);
        verify(converter).secondsToMinutes(duration);
        verify(converter).bytesToMB(size);
        verify(givenFileService).existsInRepo(fileResponse);
        verify(givenFileService).saveInRepo(fileResponse);
        verify(terminalExecutor, times(2)).removeFile(any());
    }

    @Test
    void sendFile_expectEquals_ValidYouTubeRequestInMp4() throws Exception {
        //given
        Downloader givenDownloader = youtubeDownloader;
        FileService givenFileService = youtubeFileService;
        FileRequest givenFileRequest = FileRequest.builder()
                .chatId("123")
                .extension("mp4")
                .editMessageText(new EditMessageText("123", 321, "text"))
                .build();

        int duration = 10;
        long size = 100000;
        FileResponse fileResponse = FileResponse.builder()
                .URL("https://www.youtube.com/example__id")
                .serviceId("example__id")
                .extension("mp4")
                .premium(false)
                .title("Title")
                .duration(duration)
                .size(size)
                .chatId("123")
                .multiVideoNumber(0)
                .downloadedFile(InputFile.builder().file(new File("/example")).build())
                .thumbnail(InputFile.builder().file(new File("/example/")).build())
                .build();
        SendVideo sendVideo = linkResponse.getSendVideo(fileResponse);
        Video video = Video.builder().fileId("999").build();
        Message message = Message.builder().video(video).build();

        //when
        when(givenDownloader.download(givenFileRequest)).thenReturn(fileResponse);
        when(requestService.sendRequest(sendVideo)).thenReturn(message);
        when(givenDownloader.getServiceName()).thenReturn("youtube");
        when(converter.secondsToMinutes(duration)).thenReturn("0:10");
        when(converter.bytesToMB(size)).thenReturn("1,00MB");
        when(givenFileService.existsInRepo(fileResponse)).thenReturn(false);

        linkResponse.response(givenFileRequest, givenDownloader, givenFileService);

        //then
        verify(givenDownloader).download(givenFileRequest);
        verify(requestService).sendRequest(sendVideo);
        verify(converter).secondsToMinutes(duration);
        verify(converter).bytesToMB(size);
        verify(givenFileService).existsInRepo(fileResponse);
        verify(givenFileService).saveInRepo(fileResponse);
        verify(terminalExecutor, times(2)).removeFile(any());
    }

    @Test
    void sendFile_expectEquals_YouTubeRequestInMp3WithFileIdInsteadOfDownloadedFile() throws Exception {
        //given
        Downloader givenDownloader = youtubeDownloader;
        FileService givenFileService = youtubeFileService;
        FileRequest givenFileRequest = FileRequest.builder()
                .chatId("123")
                .extension("mp3")
                .editMessageText(new EditMessageText("123", 321, "text"))
                .build();

        int duration = 10;
        long size = 100000;
        FileResponse fileResponse = FileResponse.builder()
                .URL("https://www.youtube.com/example__id")
                .serviceId("example__id")
                .extension("mp3")
                .premium(false)
                .title("Title")
                .duration(duration)
                .size(size)
                .chatId("123")
                .multiVideoNumber(0)
                .downloadedFile(InputFile.builder().fileId("fileId").build())
//                .thumbnail(InputFile.builder().file(new File("/example/")).build())
                .build();
        SendAudio sendAudio = linkResponse.getSendAudio(fileResponse);
        Audio audio = Audio.builder().fileId("999").build();
        Message message = Message.builder().audio(audio).build();

        //when
        when(givenDownloader.download(givenFileRequest)).thenReturn(fileResponse);
        when(requestService.sendRequest(sendAudio)).thenReturn(message);
        when(givenDownloader.getServiceName()).thenReturn("youtube");
        when(converter.secondsToMinutes(duration)).thenReturn("0:10");
        when(converter.bytesToMB(size)).thenReturn("1,00MB");
        when(givenFileService.existsInRepo(fileResponse)).thenReturn(true);

        linkResponse.response(givenFileRequest, givenDownloader, givenFileService);

        //then
        verify(givenDownloader).download(givenFileRequest);
        verify(requestService).sendRequest(sendAudio);
        verify(converter).secondsToMinutes(duration);
        verify(converter).bytesToMB(size);
        verify(givenFileService).existsInRepo(fileResponse);
        verify(terminalExecutor, times(0)).removeFile(any());
    }


    @Test
    void getReceivedFileInfo_expectEquals_NormalYouTubeFileResponse() {
        //given
        String serviceName = "youtube";
        int givenDuration = 10;
        long givenBytes = 10000;
        FileResponse givenFileResponse = FileResponse.builder()
                .URL("https://www.youtube.com/example__id")
                .serviceId("example__id")
                .extension("mp3")
                .premium(false)
                .title("Title")
                .duration(givenDuration)
                .size(givenBytes)
                .multiVideoNumber(0)
                .build();
        String expectedResponse = "_Here's your file_\n" +
                                  "\n" +
                                  "*Title: *Title\n" +
                                  "*Duration: *0:10\n" +
                                  "*Size: *1,00MB\n" +
                                  "*Extension: *mp3\n" +
                                  "*Quality: *standard";

        //when
        when(converter.secondsToMinutes(givenDuration)).thenReturn("0:10");
        when(converter.bytesToMB(givenBytes)).thenReturn("1,00MB");

        //then
        assertEquals(expectedResponse, linkResponse.getReceivedFileInfo(givenFileResponse, serviceName));
        verify(converter).secondsToMinutes(givenDuration);
        verify(converter).bytesToMB(givenBytes);
    }

    @Test
    void getReceivedFileInfo_expectEquals_FullMp3TitleYouTubeFileResponse() {
        //given
        String serviceName = "youtube";
        int givenDuration = 10;
        long givenBytes = 10000;
        FileResponse givenFileResponse = FileResponse.builder()
                .URL("https://www.youtube.com/example__id")
                .serviceId("example__id")
                .extension("mp3")
                .premium(false)
                .title("Title")
                .artist("Flume")
                .track("Helix")
                .album("Skin")
                .releaseYear("2016")
                .duration(givenDuration)
                .size(givenBytes)
                .multiVideoNumber(0)
                .build();
        String expectedResponse = "_Here's your file_\n" +
                                  "\n" +
                                  "*Artist: *Flume\n" +
                                  "*Track: *Helix\n" +
                                  "*Album: *Skin\n" +
                                  "*Release year: *2016\n" +
                                  "*Duration: *0:10\n" +
                                  "*Size: *1,00MB\n" +
                                  "*Extension: *mp3\n" +
                                  "*Quality: *standard";

        //when
        when(converter.secondsToMinutes(givenDuration)).thenReturn("0:10");
        when(converter.bytesToMB(givenBytes)).thenReturn("1,00MB");

        //then
        assertEquals(expectedResponse, linkResponse.getReceivedFileInfo(givenFileResponse, serviceName));
        verify(converter).secondsToMinutes(givenDuration);
        verify(converter).bytesToMB(givenBytes);
    }

    @Test
    void getReceivedFileInfo_expectEquals_NormalTwitterFileResponse() {
        //given
        String serviceName = "twitter";
        int givenDuration = 10;
        long givenBytes = 10000;
        FileResponse givenFileResponse = FileResponse.builder()
                .URL("https://www.twitter.com/example__id")
                .serviceId("example__id")
                .extension("mp3")
                .premium(true)
                .title("Title")
                .duration(givenDuration)
                .size(givenBytes)
                .multiVideoNumber(0)
                .build();
        String expectedResponse = "_Here's your file_\n" +
                                  "\n" +
                                  "*Title: *Title\n" +
                                  "*Duration: *0:10\n" +
                                  "*Size: *1,00MB\n" +
                                  "*Extension: *mp3\n" +
                                  "*Quality: *premium";

        //when
        when(converter.secondsToMinutes(givenDuration)).thenReturn("0:10");
        when(converter.bytesToMB(givenBytes)).thenReturn("1,00MB");

        //then
        assertEquals(expectedResponse, linkResponse.getReceivedFileInfo(givenFileResponse, serviceName));
        verify(converter).secondsToMinutes(givenDuration);
        verify(converter).bytesToMB(givenBytes);
    }

    @Test
    void getReceivedFileInfo_expectEquals_NoDurationInTwitterFileResponse() {
        //given
        String serviceName = "twitter";
        int givenDuration = 10;
        long givenBytes = 10000;
        FileResponse givenFileResponse = FileResponse.builder()
                .URL("https://www.twitter.com/example__id")
                .serviceId("example__id")
                .extension("mp3")
                .premium(true)
                .title("Title")
                .duration(givenDuration)
                .size(givenBytes)
                .multiVideoNumber(0)
                .build();
        String expectedResponse = "_Here's your file_\n" +
                                  "\n" +
                                  "*Title: *Title\n" +
                                  "*Size: *1,00MB\n" +
                                  "*Extension: *mp3\n" +
                                  "*Quality: *premium";

        //when
        when(converter.secondsToMinutes(givenDuration)).thenReturn("0:00");
        when(converter.bytesToMB(givenBytes)).thenReturn("1,00MB");

        //then
        assertEquals(expectedResponse, linkResponse.getReceivedFileInfo(givenFileResponse, serviceName));
        verify(converter).secondsToMinutes(givenDuration);
        verify(converter).bytesToMB(givenBytes);
    }

    @Test
    void getReceivedFileInfo_expectNotEquals_FullTitleTwitterFileResponse() {
        //given
        String serviceName = "twitter";
        int givenDuration = 10;
        long givenBytes = 10000;
        FileResponse givenFileResponse = FileResponse.builder()
                .URL("https://www.twitter.com/example__id")
                .serviceId("example__id")
                .extension("mp3")
                .premium(false)
                .title("Title")
                .artist("Flume")
                .track("Helix")
                .album("Skin")
                .releaseYear("2016")
                .duration(givenDuration)
                .size(givenBytes)
                .multiVideoNumber(0)
                .build();
        String expectedResponse = "_Here's your file_\n" +
                                  "\n" +
                                  "*Artist: *Flume\n" +
                                  "*Track: *Helix\n" +
                                  "*Album: *Skin\n" +
                                  "*Release year: *2016\n" +
                                  "*Duration: *0:10\n" +
                                  "*Size: *1,00MB\n" +
                                  "*Extension: *mp3\n" +
                                  "*Quality: *standard";

        //when
        when(converter.secondsToMinutes(givenDuration)).thenReturn("0:10");
        when(converter.bytesToMB(givenBytes)).thenReturn("1,00MB");

        //then
        assertNotEquals(expectedResponse, linkResponse.getReceivedFileInfo(givenFileResponse, serviceName));
        verify(converter).secondsToMinutes(givenDuration);
        verify(converter).bytesToMB(givenBytes);
    }

    @Test
    void getReceivedFileInfo_expectEquals_FullTitleTwitterFileResponse() {
        //given
        String serviceName = "twitter";
        int givenDuration = 10;
        long givenBytes = 10000;
        FileResponse givenFileResponse = FileResponse.builder()
                .URL("https://www.twitter.com/example__id")
                .serviceId("example__id")
                .extension("mp3")
                .premium(false)
                .title("Title")
                .artist("Flume")
                .track("Helix")
                .album("Skin")
                .releaseYear("2016")
                .duration(givenDuration)
                .size(givenBytes)
                .multiVideoNumber(0)
                .build();
        String expectedResponse = "_Here's your file_\n" +
                                  "\n" +
                                  "*Title: *Title\n" +
                                  "*Size: *1,00MB\n" +
                                  "*Extension: *mp3\n" +
                                  "*Quality: *premium";

        //when
        when(converter.secondsToMinutes(givenDuration)).thenReturn("0:10");
        when(converter.bytesToMB(givenBytes)).thenReturn("1,00MB");

        //then
        assertNotEquals(expectedResponse, linkResponse.getReceivedFileInfo(givenFileResponse, serviceName));
        verify(converter).secondsToMinutes(givenDuration);
        verify(converter).bytesToMB(givenBytes);
    }

    @Test
    void getReceivedFileInfo_expectEquals_NormalTikTokFileResponse() {
        //given
        String serviceName = "tiktok";
        int givenDuration = 10;
        long givenBytes = 10000;
        FileResponse givenFileResponse = FileResponse.builder()
                .URL("https://www.tiktok.com/example__id")
                .serviceId("example__id")
                .extension("mp3")
                .premium(false)
                .title("Title")
                .artist("Flume")
                .track("Helix")
                .album("Skin")
                .releaseYear("2016")
                .duration(givenDuration)
                .size(givenBytes)
                .multiVideoNumber(0)
                .build();
        String expectedResponse = "_Here's your file_\n" +
                                  "\n" +
                                  "*Title: *Title\n" +
                                  "*Size: *1,00MB\n" +
                                  "*Extension: *mp3\n" +
                                  "*Quality: *premium";

        //when
        when(converter.secondsToMinutes(givenDuration)).thenReturn("0:10");
        when(converter.bytesToMB(givenBytes)).thenReturn("1,00MB");

        //then
        assertNotEquals(expectedResponse, linkResponse.getReceivedFileInfo(givenFileResponse, serviceName));
        verify(converter).secondsToMinutes(givenDuration);
        verify(converter).bytesToMB(givenBytes);
    }

    @Test
    void getReceivedFileInfo_expectEquals_FullTitleInstagramFileResponse() {
        //given
        String serviceName = "instagram";
        int givenDuration = 10;
        long givenBytes = 10000;
        FileResponse givenFileResponse = FileResponse.builder()
                .URL("https://www.instagram.com/example__id")
                .serviceId("example__id")
                .extension("mp3")
                .premium(false)
                .title("Title")
                .artist("Flume")
                .track("Helix")
                .album("Skin")
                .releaseYear("2016")
                .duration(givenDuration)
                .size(givenBytes)
                .multiVideoNumber(0)
                .build();
        String expectedResponse = "_Here's your file_\n" +
                                  "\n" +
                                  "*Title: *Title\n" +
                                  "*Size: *1,00MB\n" +
                                  "*Extension: *mp3\n" +
                                  "*Quality: *premium";

        //when
        when(converter.secondsToMinutes(givenDuration)).thenReturn("0:10");
        when(converter.bytesToMB(givenBytes)).thenReturn("1,00MB");

        //then
        assertNotEquals(expectedResponse, linkResponse.getReceivedFileInfo(givenFileResponse, serviceName));
        verify(converter).secondsToMinutes(givenDuration);
        verify(converter).bytesToMB(givenBytes);
    }
}