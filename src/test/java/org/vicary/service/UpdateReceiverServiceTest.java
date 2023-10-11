package org.vicary.service;

import com.mpatric.mp3agic.ID3v1Genres;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.vicary.api_object.Update;
import org.vicary.api_object.User;
import org.vicary.api_object.chat.Chat;
import org.vicary.api_object.message.Message;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.entity.ActiveRequestEntity;
import org.vicary.entity.MessageEntity;
import org.vicary.exception.InvalidBotRequestException;
import org.vicary.info.ResponseInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.ID3TagData;
import org.vicary.pattern.Pattern;
import org.vicary.service.downloader.*;
import org.vicary.service.file_service.*;
import org.vicary.service.mapper.MessageMapper;
import org.vicary.service.mapper.UserMapper;
import org.vicary.service.quick_sender.QuickSender;
import org.vicary.service.response.AdminResponse;
import org.vicary.service.response.LinkResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UpdateReceiverServiceTest {
    @Autowired
    private UpdateReceiverService updateReceiverService;

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
    private SoundCloudDownloader soundcloudDownloader;

    @MockBean
    private InstagramFileService instagramFileService;
    @MockBean
    private TikTokFileService tiktokFileService;
    @MockBean
    private TwitterFileService twitterFileService;
    @MockBean
    private YouTubeFileService youtubeFileService;
    @MockBean
    private SoundCloudFileService soundcloudFileService;

    @MockBean
    private QuickSender quickSender;

    @MockBean
    private MessageEntityService messageEntityService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private ActiveRequestService activeRequestService;

    @MockBean
    private UserService userService;

    @MockBean
    private AdminResponse adminResponse;

    @MockBean
    private MessageMapper messageMapper;

    @MockBean
    private LinkResponse linkResponse;

    @MockBean
    private Pattern pattern;

    @Test
    void updateReceiver_expectVerifies_UpdateWithYouTubeRequest() throws Exception {
        String text = "https://www.youtube.com/watch?v=RvRhUHTV_8k -ext mp3";
        String expectedURL = "https://www.youtube.com/watch?v=RvRhUHTV_8k";
        String expectedExtension = "mp3";
        String chatId = "123456";
        Long userId = 123L;
        int messageId = 321;
        User user = new User();
        user.setId(userId);
        Chat chat = new Chat();
        chat.setId(Integer.parseInt(chatId));
        Message message = new Message();
        message.setText(text);
        message.setFrom(user);
        message.setChat(chat);

        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(info.getGotTheLink() + info.getHoldOn())
                .parseMode("MarkdownV2")
                .disableWebPagePreview(true)
                .build();

        Update update = new Update();
        update.setMessage(message);

        MessageEntity messageEntity = MessageEntity.builder().message(text).build();
        ActiveRequestEntity activeRequestEntity = new ActiveRequestEntity(userId.toString());

        Downloader expectedDownloader = youtubeDownloader;
        FileService expectedFileService = youtubeFileService;
        FileRequest expectedFileRequest = FileRequest.builder()
                .URL(expectedURL)
                .chatId(chatId)
                .extension(expectedExtension)
                .multiVideoNumber(0)
                .premium(false)
                .editMessageText(editMessageText)
                .build();
        //when
        when(messageMapper.map(update)).thenReturn(messageEntity);
        when(activeRequestService.saveActiveUser(activeRequestEntity)).thenReturn(activeRequestEntity);
        when(pattern.isYouTubeURL(expectedURL)).thenReturn(true);
        when(quickSender.messageWithReturn(chatId, editMessageText.getText(), true)).thenReturn(Message.builder().messageId(messageId).build());
        when(expectedDownloader.getAvailableExtensions()).thenReturn(List.of("mp3", "mp4", "flac"));

        System.out.println("Expected downloader: " + expectedDownloader);

        updateReceiverService.updateReceiver(update);

        //then
        verify(messageEntityService).save(messageEntity);
        verify(userService).existsByUserId(userId.toString());
        verify(activeRequestService).existsByUserId(userId.toString());
        verify(activeRequestService).saveActiveUser(activeRequestEntity);
        verify(activeRequestService).deleteById(null);
        verify(linkResponse).response(expectedFileRequest, expectedDownloader, expectedFileService);
    }

    @Test
    void updateReceiver_expectVerifies_UpdateWithTwitterRequest() throws Exception {
        String text = "https://www.twitter.com -ext mp4";
        String expectedURL = "https://www.twitter.com";
        String expectedExtension = "mp4";
        String chatId = "123456";
        Long userId = 123L;
        int messageId = 321;
        User user = new User();
        user.setId(userId);
        Chat chat = new Chat();
        chat.setId(Integer.parseInt(chatId));
        Message message = new Message();
        message.setText(text);
        message.setFrom(user);
        message.setChat(chat);

        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(info.getGotTheLink() + info.getHoldOn())
                .parseMode("MarkdownV2")
                .disableWebPagePreview(true)
                .build();

        Update update = new Update();
        update.setMessage(message);

        MessageEntity messageEntity = MessageEntity.builder().message(text).build();
        ActiveRequestEntity activeRequestEntity = new ActiveRequestEntity(userId.toString());

        Downloader expectedDownloader = twitterDownloader;
        FileService expectedFileService = twitterFileService;
        FileRequest expectedFileRequest = FileRequest.builder()
                .URL(expectedURL)
                .chatId(chatId)
                .extension(expectedExtension)
                .multiVideoNumber(0)
                .premium(false)
                .editMessageText(editMessageText)
                .build();
        //when
        when(messageMapper.map(update)).thenReturn(messageEntity);
        when(activeRequestService.saveActiveUser(activeRequestEntity)).thenReturn(activeRequestEntity);
        when(pattern.isTwitterURL(expectedURL)).thenReturn(true);
        when(quickSender.messageWithReturn(chatId, editMessageText.getText(), true)).thenReturn(Message.builder().messageId(messageId).build());
        when(expectedDownloader.getAvailableExtensions()).thenReturn(List.of("mp3", "mp4", "flac"));

        updateReceiverService.updateReceiver(update);

        //then
        verify(messageEntityService).save(messageEntity);
        verify(userService).existsByUserId(userId.toString());
        verify(activeRequestService).existsByUserId(userId.toString());
        verify(activeRequestService).saveActiveUser(activeRequestEntity);
        verify(activeRequestService).deleteById(null);
        verify(linkResponse).response(expectedFileRequest, expectedDownloader, expectedFileService);
    }

    @Test
    void updateReceiver_expectVerifies_UpdateWithTikTokRequest() throws Exception {
        String text = "https://www.tiktok.com -ext mp4";
        String expectedURL = "https://www.tiktok.com";
        String expectedExtension = "mp4";
        String chatId = "123456";
        Long userId = 123L;
        int messageId = 321;
        User user = new User();
        user.setId(userId);
        Chat chat = new Chat();
        chat.setId(Integer.parseInt(chatId));
        Message message = new Message();
        message.setText(text);
        message.setFrom(user);
        message.setChat(chat);

        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(info.getGotTheLink() + info.getHoldOn())
                .parseMode("MarkdownV2")
                .disableWebPagePreview(true)
                .build();

        Update update = new Update();
        update.setMessage(message);

        MessageEntity messageEntity = MessageEntity.builder().message(text).build();
        ActiveRequestEntity activeRequestEntity = new ActiveRequestEntity(userId.toString());

        Downloader expectedDownloader = tiktokDownloader;
        FileService expectedFileService = tiktokFileService;
        FileRequest expectedFileRequest = FileRequest.builder()
                .URL(expectedURL)
                .chatId(chatId)
                .extension(expectedExtension)
                .multiVideoNumber(0)
                .premium(false)
                .editMessageText(editMessageText)
                .build();
        //when
        when(messageMapper.map(update)).thenReturn(messageEntity);
        when(activeRequestService.saveActiveUser(activeRequestEntity)).thenReturn(activeRequestEntity);
        when(pattern.isTikTokURL(expectedURL)).thenReturn(true);
        when(quickSender.messageWithReturn(chatId, editMessageText.getText(), true)).thenReturn(Message.builder().messageId(messageId).build());
        when(expectedDownloader.getAvailableExtensions()).thenReturn(List.of("mp3", "mp4", "flac"));

        updateReceiverService.updateReceiver(update);

        //then
        verify(messageEntityService).save(messageEntity);
        verify(userService).existsByUserId(userId.toString());
        verify(activeRequestService).existsByUserId(userId.toString());
        verify(activeRequestService).saveActiveUser(activeRequestEntity);
        verify(activeRequestService).deleteById(null);
        verify(linkResponse).response(expectedFileRequest, expectedDownloader, expectedFileService);
    }

    @Test
    void updateReceiver_expectVerifies_UpdateWithInstagramRequest() throws Exception {
        String text = "https://www.instagram.com -ext mp4";
        String expectedURL = "https://www.instagram.com";
        String expectedExtension = "mp4";
        String chatId = "123456";
        Long userId = 123L;
        int messageId = 321;
        User user = new User();
        user.setId(userId);
        Chat chat = new Chat();
        chat.setId(Integer.parseInt(chatId));
        Message message = new Message();
        message.setText(text);
        message.setFrom(user);
        message.setChat(chat);

        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(info.getGotTheLink() + info.getHoldOn())
                .parseMode("MarkdownV2")
                .disableWebPagePreview(true)
                .build();

        Update update = new Update();
        update.setMessage(message);

        MessageEntity messageEntity = MessageEntity.builder().message(text).build();
        ActiveRequestEntity activeRequestEntity = new ActiveRequestEntity(userId.toString());

        Downloader expectedDownloader = instagramDownloader;
        FileService expectedFileService = instagramFileService;
        FileRequest expectedFileRequest = FileRequest.builder()
                .URL(expectedURL)
                .chatId(chatId)
                .extension(expectedExtension)
                .multiVideoNumber(0)
                .premium(false)
                .editMessageText(editMessageText)
                .build();
        //when
        when(messageMapper.map(update)).thenReturn(messageEntity);
        when(activeRequestService.saveActiveUser(activeRequestEntity)).thenReturn(activeRequestEntity);
        when(pattern.isInstagramURL(expectedURL)).thenReturn(true);
        when(quickSender.messageWithReturn(chatId, editMessageText.getText(), true)).thenReturn(Message.builder().messageId(messageId).build());
        when(expectedDownloader.getAvailableExtensions()).thenReturn(List.of("mp3", "mp4", "flac"));

        updateReceiverService.updateReceiver(update);

        //then
        verify(pattern).isInstagramURL(expectedURL);
        verify(messageEntityService).save(messageEntity);
        verify(userService).existsByUserId(userId.toString());
        verify(activeRequestService).existsByUserId(userId.toString());
        verify(activeRequestService).saveActiveUser(activeRequestEntity);
        verify(activeRequestService).deleteById(null);
        verify(linkResponse).response(expectedFileRequest, expectedDownloader, expectedFileService);
    }

    @Test
    void updateReceiver_expectThrowInvalidBotRequest_UpdateWithoutMessage() {
        //given
        Update update = new Update();
        //when
        //then
        assertDoesNotThrow(() -> updateReceiverService.updateReceiver(update));

        verify(messageEntityService, never()).save(any());
    }

    @Test
    void getFileRequest_expectVerify_NoURLInMessage() throws Exception {
        String text = "hello";
        String expectedURL = "hello";
        String chatId = "123456";
        Long userId = 123L;
        int messageId = 321;
        User user = new User();
        user.setId(userId);
        Chat chat = new Chat();
        chat.setId(Integer.parseInt(chatId));
        Message message = new Message();
        message.setText(text);
        message.setFrom(user);
        message.setChat(chat);

        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(info.getGotTheLink() + info.getHoldOn())
                .parseMode("MarkdownV2")
                .disableWebPagePreview(true)
                .build();

        Update update = new Update();
        update.setMessage(message);

        MessageEntity messageEntity = MessageEntity.builder().message(text).build();
        ActiveRequestEntity activeRequestEntity = new ActiveRequestEntity(userId.toString());

        //when
        when(messageMapper.map(update)).thenReturn(messageEntity);
        when(activeRequestService.saveActiveUser(activeRequestEntity)).thenReturn(activeRequestEntity);
        when(pattern.isInstagramURL(expectedURL)).thenReturn(false);
        when(pattern.isYouTubeURL(expectedURL)).thenReturn(false);
        when(pattern.isTwitterURL(expectedURL)).thenReturn(false);
        when(pattern.isSoundCloudURL(expectedURL)).thenReturn(false);
        when(pattern.isTikTokURL(expectedURL)).thenReturn(false);
        when(quickSender.messageWithReturn(chatId, editMessageText.getText(), true)).thenReturn(Message.builder().messageId(messageId).build());

        updateReceiverService.updateReceiver(update);

        //then
        verify(pattern).isInstagramURL(expectedURL);
        verify(messageEntityService).save(messageEntity);
        verify(userService).existsByUserId(userId.toString());
        verify(activeRequestService).existsByUserId(userId.toString());
        verify(activeRequestService).saveActiveUser(activeRequestEntity);
        verify(activeRequestService).deleteById(null);
        verify(pattern).isInstagramURL(expectedURL);
        verify(pattern).isYouTubeURL(expectedURL);
        verify(pattern).isSoundCloudURL(expectedURL);
        verify(pattern).isTwitterURL(expectedURL);
        verify(pattern).isTikTokURL(expectedURL);
        verify(linkResponse, times(0)).response(any(), any(), any());

    }

    @Test
    void getMultiVideoNumber_expectEquals_ValidNumber() {
        //given
        String givenText = "mul 2";
        int expectedNumber = 2;

        //when
        int actualNumber = updateReceiverService.getMultiVideoNumber(givenText);

        //then
        assertEquals(expectedNumber, actualNumber);
    }


    @Test
    void getMultiVideoNumber_expectThrow_InvalidNumberNoInteger() {
        //given
        String givenText = "mul STRING";

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> updateReceiverService.getMultiVideoNumber(givenText));
    }

    @Test
    void getMultiVideoNumber_expectThrow_GiganticNumber() {
        //given
        String givenText = "mul 949419491949135553252529955";

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> updateReceiverService.getMultiVideoNumber(givenText));
    }

    @Test
    void getMultiVideoNumber_expectEquals_NoNumber() {
        //given
        String givenText = "mul ";

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> updateReceiverService.getMultiVideoNumber(givenText));
    }

    @Test
    void getMultiVideoNumber_expectEquals_NegativeNumber() {
        //given
        String text = "mul -12";
        int expectedNumber = 0;

        //when
        int actualNumber = updateReceiverService.getMultiVideoNumber(text);
        //then
        assertEquals(expectedNumber, actualNumber);
    }


    @Test
    void getExtension_expectEquals_ValidExtension() {
        //given
        List<String> givenListOfExtensions = List.of("mp3", "mp4", "flac");
        String givenText = "ext mp3";
        String expectedExtension = "mp3";

        //when
        String actualExtension = updateReceiverService.getExtension(givenText, givenListOfExtensions);

        //then
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    void getExtension_expectThrows_InvalidExtension() {
        //given
        String givenText = "ext avi";
        List<String> givenListOfExtensions = List.of("mp3", "mp4", "flac");

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> updateReceiverService.getExtension(givenText, givenListOfExtensions));
    }

    @Test
    void getExtension_expectThrows_EmptyExtension() {
        //given
        String givenText = "ext ";
        List<String> givenListOfExtensions = List.of("mp3", "mp4", "flac");

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> updateReceiverService.getExtension(givenText, givenListOfExtensions));
    }


    @Test
    void getExtension_expectThrows_ValidExtensionWithRandomWordsAfter() {
        //given
        String givenText = "ext mp3 hello";
        List<String> givenListOfExtensions = List.of("mp3", "mp4", "flac");

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> updateReceiverService.getExtension(givenText, givenListOfExtensions));
    }


    @Test
    void getURL_expectEquals_NormalURL() {
        //given
        String givenText = "https://url.com/hello -ext mp3 -tag asd:adsd";
        String expectedURL = "https://url.com/hello";

        //when
        String actualURL = updateReceiverService.getURL(givenText);

        //then
        assertEquals(expectedURL, actualURL);
    }

    @Test
    void getURL_expectThrows_EmptyTextButNotPossible() {
        //given
        String givenText = "";
        String expectedURL = "";

        //when
        String actualURL = updateReceiverService.getURL(givenText);

        //then
        assertEquals(expectedURL, actualURL);
    }


    @Test
    void getId3Tag_expectEquals_ValidText() {
        //given
        String givenText = "tag Flume:Helix:Skin:2018:Electronic";
        ID3TagData expectedId3Tag = ID3TagData.builder()
                .artist("Flume")
                .title("Helix")
                .album("Skin")
                .releaseYear("2018")
                .genre(ID3v1Genres.matchGenreDescription("Electronic"))
                .build();

        //when
        ID3TagData actualId3Tag = updateReceiverService.getId3Tag(givenText);

        //then
        assertEquals(expectedId3Tag, actualId3Tag);
    }


    @Test
    void getId3Tag_expectEquals_ValidTextOnlyArtistAndTitle() {
        //given
        String givenText = "tag Flume:Helix";
        ID3TagData expectedId3Tag = ID3TagData.builder()
                .artist("Flume")
                .title("Helix")
                .build();

        //when
        ID3TagData actualId3Tag = updateReceiverService.getId3Tag(givenText);

        //then
        assertEquals(expectedId3Tag, actualId3Tag);
    }


    @Test
    void getId3Tag_expectEquals_ValidTextOnlyReleaseYear() {
        //given
        String givenText = "tag :::2018";
        ID3TagData expectedId3Tag = ID3TagData.builder()
                .releaseYear("2018")
                .build();

        //when
        ID3TagData actualId3Tag = updateReceiverService.getId3Tag(givenText);

        //then
        assertEquals(expectedId3Tag, actualId3Tag);
    }


    @Test
    void getId3Tag_expectEquals_TextContains11DASH11() {
        //given
        String givenText = "tag Flume:Hel11DASH11ix:Skin:2018:Electronic";
        ID3TagData expectedId3Tag = ID3TagData.builder()
                .artist("Flume")
                .title("Hel-ix")
                .album("Skin")
                .releaseYear("2018")
                .genre(ID3v1Genres.matchGenreDescription("Electronic"))
                .build();

        //when
        ID3TagData actualId3Tag = updateReceiverService.getId3Tag(givenText);

        //then
        assertEquals(expectedId3Tag, actualId3Tag);
    }


    @Test
    void getId3Tag_expectEquals_TextWithoutColons() {
        //given
        String givenText = "tag Flume";
        ID3TagData expectedId3Tag = ID3TagData.builder()
                .artist("Flume")
                .build();

        //when
        ID3TagData actualId3Tag = updateReceiverService.getId3Tag(givenText);

        //then
        assertEquals(expectedId3Tag, actualId3Tag);
    }


    @Test
    void getId3Tag_expectNull_NoTagsInText() {
        //given
        String givenText = "tag ";

        //when
        ID3TagData actualId3Tag = updateReceiverService.getId3Tag(givenText);

        //then
        assertNull(actualId3Tag);
    }


    @Test
    void getId3Tag_expectThrow_InvalidGenre() {
        //given
        String givenText = "tag Flume:Helix:Skin:2018:SomeWrongGenre";

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> updateReceiverService.getId3Tag(givenText));
    }


    @Test
    void getId3Tag_expectThrow_TagMoreThan59Chars() {
        //given
        String givenText = "tag FlumeFlumeFlumeFlumeFlumeFlumeFlumeFlumeFlumeFlumeFlumeFlume:Helix:Skin:2018";

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> updateReceiverService.getId3Tag(givenText));
    }


    @Test
    void getId3Tag_expectEquals_ReleasedYearAsString() {
        //given
        String givenText = "tag Flume:Helix:Skin:Year:Electronic";
        ID3TagData expectedId3Tag = ID3TagData.builder()
                .artist("Flume")
                .title("Helix")
                .album("Skin")
                .releaseYear("Year")
                .genre(ID3v1Genres.matchGenreDescription("Electronic"))
                .build();

        //when
        ID3TagData actualId3Tag = updateReceiverService.getId3Tag(givenText);

        //then
        assertEquals(expectedId3Tag, actualId3Tag);
    }


    @Test
    void replaceDashesTo11DASH11_expectEquals_NormalText() {
        //given
        String givenText = "-ext mp3 -tag Flume:Helix:Skin:2018:Electronic";
        String expectedText = "-ext mp3 -tag Flume:Helix:Skin:2018:Electronic";

        //when
        String actualText = updateReceiverService.replaceDashesTo11DASH11(givenText);

        //then
        assertEquals(expectedText, actualText);
    }


    @Test
    void replaceDashesTo11DASH11_expectEquals_TagWithDash() {
        //given
        String givenText = "-ext mp3 -tag Flume:Hel-ix:Skin:2018:Electronic";
        String expectedText = "-ext mp3 -tag Flume:Hel11DASH11ix:Skin:2018:Electronic";

        //when
        String actualText = updateReceiverService.replaceDashesTo11DASH11(givenText);

        //then
        assertEquals(expectedText, actualText);
    }


    @Test
    void replaceDashesTo11DASH11_expectEquals_ExtensionTagAndMultiVideo() {
        //given
        String givenText = "-ext -tag -mul ";
        String expectedText = "-ext -tag -mul ";

        //when
        String actualText = updateReceiverService.replaceDashesTo11DASH11(givenText);

        //then
        assertEquals(expectedText, actualText);
    }


    @Test
    void replaceDashesTo11DASH11_expectEquals_RandomDashes() {
        //given
        String givenText = "hell-o - what is going - on-?";
        String expectedText = "hell11DASH11o 11DASH11 what is going 11DASH11 on11DASH11?";

        //when
        String actualText = updateReceiverService.replaceDashesTo11DASH11(givenText);

        //then
        assertEquals(expectedText, actualText);
    }


    @Test
    void removeUrl_expectEquals_NormalText() {
        //given
        String givenText = "https://someurl.com/asd -ext mp3 -tag asd:asd";
        String expectedText = "-ext mp3 -tag asd:asd";

        //when
        String actualText = updateReceiverService.removeUrl(givenText);

        //then
        assertEquals(expectedText, actualText);
    }


    @Test
    void removeUrl_expectEquals_WeirdText() {
        //given
        String givenText = "what is going on?";
        String expectedText = "is going on?";

        //when
        String actualText = updateReceiverService.removeUrl(givenText);

        //then
        assertEquals(expectedText, actualText);
    }


    @Test
    void removeUrl_expectEquals_EmptyText() {
        //given
        String givenText = "";
        String expectedText = "";

        //when
        String actualText = updateReceiverService.removeUrl(givenText);

        //then
        assertEquals(expectedText, actualText);
    }


    @Test
    void getFileRequest_expectEquals_NormalParams() {
        //given
        String text = "https://www.URL.com mp4 #4";
        String chatId = "123456";
        Long userId = 123L;
        int messageId = 321;
        User user = new User();
        user.setId(userId);
        Chat chat = new Chat();
        chat.setId(Integer.parseInt(chatId));
        Message message = new Message();
        message.setText(text);
        message.setFrom(user);
        message.setChat(chat);

        Update update = new Update();
        update.setMessage(message);
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(update.getChatId())
                .messageId(messageId)
                .text(info.getGotTheLink() + info.getHoldOn())
                .parseMode("MarkdownV2")
                .disableWebPagePreview(true)
                .build();

    }
}

























