package org.vicary.service.quick_sender;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.api_request.send.SendChatAction;
import org.vicary.api_request.send.SendMessage;
import org.vicary.service.RequestService;

import static org.mockito.Mockito.*;

@RunWith(Runner.class)
@SpringBootTest
class QuickSenderTest {

    @Autowired
    private QuickSender quickSender;

    @MockBean
    private RequestService requestService;

    @Test
    void message_expectVerify_ValidParams() {
        //given
        String givenChatId = "chatId";
        String givenText = "example";
        boolean givenMarkdownV2 = true;

        SendMessage expectedSendMessage = SendMessage.builder()
                .chatId(givenChatId)
                .text(givenText)
                .parseMode(givenMarkdownV2 ? "MarkdownV2" : "")
                .build();

        //when
        quickSender.message(givenChatId, givenText, givenMarkdownV2);

        //then
        verify(requestService).sendRequestAsync(expectedSendMessage);
    }

    @Test
    void message_expectVerify_NullParams() {
        //given
        String givenChatId = null;
        String givenText = null;
        boolean givenMarkdownV2 = true;

        //when
        quickSender.message(givenChatId, givenText, givenMarkdownV2);

        //then
        verify(requestService, times(0)).sendRequestAsync(any());
    }

    @Test
    void messageWithReturn_expectVerify_ValidParams() {
        //given
        String givenChatId = "chatId";
        String givenText = "example";
        boolean givenMarkdownV2 = true;

        SendMessage expectedSendMessage = SendMessage.builder()
                .chatId(givenChatId)
                .text(givenText)
                .parseMode(givenMarkdownV2 ? "MarkdownV2" : "")
                .build();

        //when
        quickSender.messageWithReturn(givenChatId, givenText, givenMarkdownV2);

        //then
        verify(requestService).sendRequest(expectedSendMessage);
    }

    @Test
    void messageWithReturn_expectVerify_NullParams() {
        //given
        String givenChatId = null;
        String givenText = null;
        boolean givenMarkdownV2 = true;

        //when
        quickSender.messageWithReturn(givenChatId, givenText, givenMarkdownV2);

        //then
        verify(requestService, times(0)).sendRequest((SendMessage) any());
    }

    @Test
    void editMessageText_expectVerify_ValidParams() {
        //given
        String givenChatId = "chatId";
        String givenText = "example";
        int givenMessageId = 123;

        EditMessageText givenEditMessageText = EditMessageText.builder()
                .chatId(givenChatId)
                .text("some text")
                .messageId(givenMessageId)
                .build();

        //when
        quickSender.editMessageText(givenEditMessageText, givenText);

        //then
        verify(requestService).sendRequestAsync(givenEditMessageText);
    }

    @Test
    void editMessageText_expectVerify_NullParams() {
        //given
        String givenText = null;

        EditMessageText givenEditMessageText = null;

        //when
        quickSender.editMessageText(givenEditMessageText, givenText);

        //then
        verify(requestService, times(0)).sendRequestAsync(any());
    }

    @Test
    void chatAction_expectVerify_ValidParams() {
        //given
        String givenChatId = "chatId";
        String givenAction = "some action - typing for example";

        SendChatAction expectedSendChatAction = SendChatAction.builder()
                .chatId(givenChatId)
                .action(givenAction)
                .build();

        //when
        quickSender.chatAction(givenChatId, givenAction);

        //then
        verify(requestService).sendRequestAsync(expectedSendChatAction);
    }

    @Test
    void chatAction_expectVerify_NullParams() {
        //given
        String givenChatId = null;
        String givenAction = null;

        //when
        quickSender.chatAction(givenChatId, givenAction);

        //then
        verify(requestService, times(0)).sendRequestAsync(any());
    }
}