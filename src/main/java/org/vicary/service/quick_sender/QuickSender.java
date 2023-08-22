package org.vicary.service.quick_sender;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.vicary.api_object.message.Message;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.api_request.send.SendChatAction;
import org.vicary.api_request.send.SendMessage;
import org.vicary.service.RequestService;

@RequiredArgsConstructor
@Component
public class QuickSender {
    private static final Logger logger = LoggerFactory.getLogger(QuickSender.class);

    private final RequestService requestService;

    public void message(String chatId, String text, boolean markdownV2) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode(markdownV2 ? "MarkdownV2" : "")
                .build();
        try {
            requestService.sendRequestAsync(sendMessage);
        } catch (RestClientException ex) {
            logger.warn("Error in sending message request, message: {}", ex.getMessage());
        }
    }

    public Message messageWithReturn(String chatId, String text, boolean markdownV2) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode(markdownV2 ? "MarkdownV2" : "")
                .build();
        try {
            return requestService.sendRequest(sendMessage);
        } catch (WebClientRequestException | WebClientResponseException ex) {
            logger.warn("Error in sending message with return request, message: {}", ex.getMessage());
        }
        return null;
    }

    public void editMessageText(EditMessageText editMessageText, String text) {
        editMessageText.setText(text);
        try {
            requestService.sendRequestAsync(editMessageText);
        } catch (RestClientException ex) {
            logger.warn("Error in sending editMessageText request, message: {}", ex.getMessage());
        }
    }

    public void chatAction(String chatId, String action) {
        SendChatAction sendChatAction = SendChatAction.builder()
                .chatId(chatId)
                .action(action)
                .build();
        try {
            requestService.sendRequestAsync(sendChatAction);
        } catch (RestClientException ex) {
            logger.warn("Error in sending chatAction request, message: {}", ex.getMessage());
        }
    }
}
