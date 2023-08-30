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
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .parseMode(markdownV2 ? "MarkdownV2" : "")
                    .build();
            requestService.sendRequestAsync(sendMessage);
        } catch (Exception ex) {
            logger.warn("Error in sending message request, message: {}", ex.getMessage());
        }
    }

    public Message messageWithReturn(String chatId, String text, boolean markdownV2) {
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .parseMode(markdownV2 ? "MarkdownV2" : "")
                    .build();
            return requestService.sendRequest(sendMessage);
        } catch (Exception ex) {
            logger.warn("Error in sending message with return request, message: {}", ex.getMessage());
            return null;
        }
    }

    public void editMessageText(EditMessageText editMessageText, String text) {
        try {
            editMessageText.setText(text);
            requestService.sendRequestAsync(editMessageText);
        } catch (Exception ex) {
            logger.warn("Error in sending editMessageText request, message: {}", ex.getMessage());
        }
    }

    public void chatAction(String chatId, String action) {
        try {
            SendChatAction sendChatAction = SendChatAction.builder()
                    .chatId(chatId)
                    .action(action)
                    .build();
            requestService.sendRequestAsync(sendChatAction);
        } catch (Exception ex) {
            logger.warn("Error in sending chatAction request, message: {}", ex.getMessage());
        }
    }
}
