package org.vicary.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
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

@RunWith(Runner.class)
@SpringBootTest
class UpdateReceiverServiceTest {
    @Autowired
    private UpdateReceiverService updateReceiverService;

    @Autowired
    private YouTubeDownloader youtubeDownloader;
    @Autowired
    private TwitterDownloader twitterDownloader;
    @Autowired
    private TikTokDownloader tiktokDownloader;
    @Autowired
    private InstagramDownloader instagramDownloader;

    @Autowired
    private InstagramFileService instagramFileService;
    @Autowired
    private TikTokFileService tiktokFileService;
    @Autowired
    private TwitterFileService twitterFileService;
    @Autowired
    private YouTubeFileService youtubeFileService;

    @Autowired
    private ResponseInfo info;


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
    void updateReceiver_expectVerifies_UpdateWithYouTubeRequest() {
        String text = "https://www.youtube.com/watch?v=RvRhUHTV_8k mp3";
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
        FileRequest expectedFileRequest = Filerequest.builder()
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

        updateReceiverService.updateReceiver(update);

        //then
        verify(messageEntityService).save(messageEntity);
        verify(userService).existsByUserId(userId.toString());
        verify(activeRequestService).existsByUserId(userId.toString());
        verify(activeRequestService).saveActiveUser(activeRequestEntity);
        try {
            verify(linkResponse).sendFile(expectedFileRequest, expectedDownloader, expectedFileService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateReceiver_expectVerifies_UpdateWithTwitterRequest() {
        String text = "https://www.twitter.com";
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

        updateReceiverService.updateReceiver(update);

        //then
        verify(messageEntityService).save(messageEntity);
        verify(userService).existsByUserId(userId.toString());
        verify(activeRequestService).existsByUserId(userId.toString());
        verify(activeRequestService).saveActiveUser(activeRequestEntity);
        try {
            verify(linkResponse).sendFile(expectedFileRequest, expectedDownloader, expectedFileService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateReceiver_expectVerifies_UpdateWithTikTokRequest() {
        String text = "https://www.tiktok.com";
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

        updateReceiverService.updateReceiver(update);

        //then
        verify(messageEntityService).save(messageEntity);
        verify(userService).existsByUserId(userId.toString());
        verify(activeRequestService).existsByUserId(userId.toString());
        verify(activeRequestService).saveActiveUser(activeRequestEntity);
        try {
            verify(linkResponse).sendFile(expectedFileRequest, expectedDownloader, expectedFileService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateReceiver_expectVerifies_UpdateWithInstagramRequest() {
        String text = "https://www.instagram.com";
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

        updateReceiverService.updateReceiver(update);

        //then
        verify(messageEntityService).save(messageEntity);
        verify(userService).existsByUserId(userId.toString());
        verify(activeRequestService).existsByUserId(userId.toString());
        verify(activeRequestService).saveActiveUser(activeRequestEntity);
        try {
            verify(linkResponse).sendFile(expectedFileRequest, expectedDownloader, expectedFileService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateReceiver_expectThrowInvalidBotRequest_UpdateWithoutMessage() {
        //given
        Update update = new Update();
        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> updateReceiverService.updateReceiver(update));
    }

    @Test
    void getFileRequest_expectEquals_ValidParams() {
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
        Downloader downloader = new TwitterDownloader(null, null, null, null, null, null, new Converter(), new FileManager(new Converter()));
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(update.getChatId())
                .messageId(messageId)
                .text(info.getGotTheLink() + info.getHoldOn())
                .parseMode("MarkdownV2")
                .disableWebPagePreview(true)
                .build();

        String expectedUrl = "https://www.URL.com";
        String expectedChatId = "123456";
        String expectedExtension = "mp4";
        int expectedMultiVideoNumber = 4;
        boolean expectedPremium = false;
        EditMessageText expectedEMT = editMessageText;
        FileRequest expectedFileRequest = FileRequest.builder()
                .URL(expectedUrl)
                .chatId(expectedChatId)
                .extension(expectedExtension)
                .multiVideoNumber(expectedMultiVideoNumber)
                .premium(expectedPremium)
                .editMessageText(expectedEMT)
                .build();

        //when
        FileRequest actualFileRequest = updateReceiverService.getFileRequest(update, downloader, messageId);

        //then
        assertEquals(expectedFileRequest, actualFileRequest);
        verify(userService).findByUserId(userId.toString());

    }

    @Test
    void getMultiVideoNumber_expectEquals_ValidNumber() {
        //given
        String text = "www.link.com #2";
        int expectedNumber = 2;

        //when
        int actualNumber = updateReceiverService.getMultiVideoNumber(text);

        //then
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    void getMultiVideoNumber_expectEquals_ValidNumberWithExtensionBehind() {
        //given
        String text = "www.link.com mp3 #2";
        int expectedNumber = 2;

        //when
        int actualNumber = updateReceiverService.getMultiVideoNumber(text);

        //then
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    void getMultiVideoNumber_expectEquals_ValidNumberWithExtensionAfter() {
        //given
        String text = "www.link.com #2 mp3";
        int expectedNumber = 2;

        //when
        int actualNumber = updateReceiverService.getMultiVideoNumber(text);

        //then
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    void getMultiVideoNumber_expectEquals_NumberWithoutHash() {
        //given
        String text = "www.link.com 2";
        int expectedNumber = 0;

        //when
        int actualNumber = updateReceiverService.getMultiVideoNumber(text);
        //then
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    void getMultiVideoNumber_expectEquals_NoNumber() {
        //given
        String text = "www.link.com";
        int expectedNumber = 0;

        //when
        int actualNumber = updateReceiverService.getMultiVideoNumber(text);
        //then
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    void getMultiVideoNumber_expectEquals_NegativeNumber() {
        //given
        String text = "www.link.com #-12";
        int expectedNumber = 0;

        //when
        int actualNumber = updateReceiverService.getMultiVideoNumber(text);
        //then
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    void getMultiVideoNumber_expectEquals_TooBigForIntegerNumber() {
        //given
        String text = "www.link.com #9999999999999999999";

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> updateReceiverService.getMultiVideoNumber(text));
    }

    @Test
    void getMultiVideoNumber_expectThrowInvalidBotRequest_InvalidNumber() {
        //given
        String text = "www.link.com #284jf";

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> updateReceiverService.getMultiVideoNumber(text));
    }

    @Test
    void getExtension_expectEquals_ValidExtension() {
        //given
        String text = "www.link.com mp4";
        List<String> availableExtensions = List.of("mp3", "mp4", "flac");
        String expectedExtension = "mp4";

        //when
        String actualExtension = updateReceiverService.getExtension(text, availableExtensions);

        //then
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    void getExtension_expectEquals_ValidExtensionWithRandomWordsAfter() {
        //given
        String text = "www.link.com flac #2 test random mp3";
        List<String> availableExtensions = List.of("mp3", "mp4", "flac");
        String expectedExtension = "flac";

        //when
        String actualExtension = updateReceiverService.getExtension(text, availableExtensions);

        //then
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    void getExtension_expectEquals_ValidExtensionWithMultiVideoSpecify() {
        //given
        String text = "www.link.com #2 flac";
        List<String> availableExtensions = List.of("mp3", "mp4", "flac");
        String expectedExtension = "flac";

        //when
        String actualExtension = updateReceiverService.getExtension(text, availableExtensions);

        //then
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    void getExtension_expectEquals_InvalidExtensionWithMultiVideoSpecify() {
        //given
        String text = "www.link.com #2 ext";
        List<String> availableExtensions = List.of("mp3", "mp4", "flac");
        String expectedExtension = "mp3";

        //when
        String actualExtension = updateReceiverService.getExtension(text, availableExtensions);

        //then
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    void getExtension_expectEquals_NoExtensionWithMultiVideoSpecify() {
        //given
        String text = "www.link.com #2";
        List<String> availableExtensions = List.of("mp3", "mp4", "flac");
        String expectedExtension = "mp3";

        //when
        String actualExtension = updateReceiverService.getExtension(text, availableExtensions);

        //then
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    void getExtension_expectEquals_NoExtensionSpecify() {
        //given
        String text = "www.link.com";
        List<String> availableExtensions = List.of("mp3", "mp4", "flac");
        String expectedExtension = "mp3";

        //when
        String actualExtension = updateReceiverService.getExtension(text, availableExtensions);

        //then
        assertEquals(expectedExtension, actualExtension);
    }

    @Test
    void getExtension_expectThrowInvalidBotRequest_InvalidExtensionSpecify() {
        //given
        String text = "www.link.com ext";
        List<String> availableExtensions = List.of("mp3", "mp4", "flac");

        //when
        //then
        assertThrows(InvalidBotRequestException.class, () -> updateReceiverService.getExtension(text, availableExtensions));
    }
}