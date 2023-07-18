package org.example.api_request.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.message.Message;
import org.example.api_object.message.MessageEntity;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SendPoll implements ApiRequest<Message> {
    /**
     * Use this method to send a native poll. On success, the sent Message is returned.
     *
     * @param chatId                Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId       Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param question              Poll question, 1-300 characters.
     * @param options               A list of answer options, 2-10 strings, 1-100 characters each.
     * @param isAnonymous           Optional. True, if the poll needs to be anonymous, defaults to True.
     * @param type                  Optional. Poll type, “quiz” or “regular”, defaults to “regular”.
     * @param allowsMultipleAnswers Optional. True, if the poll allows multiple answers, ignored for polls in quiz mode, defaults to False.
     * @param correctOptionId       Optional. 0-based identifier of the correct answer option, required for polls in quiz mode.
     * @param explanation           Optional. Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200 characters with at most 2 line feeds after entities parsing.
     * @param explanationParseMode  Optional. Mode for parsing entities in the explanation. See formatting options for more details.
     * @param explanationEntities   Optional. A list of special entities that appear in the poll explanation, which can be specified instead of parse_mode.
     * @param openPeriod            Optional. Amount of time in seconds the poll will be active after creation, 5-600. Can't be used together with close_date.
     * @param closeDate             Optional. Point in time (Unix timestamp) when the poll will be automatically closed. Must be at least 5 and no more than 600 seconds in the future. Can't be used together with open_period.
     * @param isClosed              Optional. Pass True if the poll needs to be immediately closed. This can be useful for poll preview.
     * @param disableNotification   Optional. Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent        Optional. Protects the contents of the sent message from forwarding and saving.
     * @param replyToMessageId      Optional. If the message is a reply, ID of the original message.
     * @param allowSendingWithoutReply Optional. Pass True if the message should be sent even if the specified replied-to message is not found.
     */

    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @NonNull
    @JsonProperty("question")
    private String question;

    @NonNull
    @JsonProperty("options")
    private List<String> options;

    @JsonProperty("is_anonymous")
    private Boolean isAnonymous;

    @JsonProperty("type")
    private String type;

    @JsonProperty("allows_multiple_answers")
    private Boolean allowsMultipleAnswers;

    @JsonProperty("correct_option_id")
    private Integer correctOptionId;

    @JsonProperty("explanation")
    private String explanation;

    @JsonProperty("explanation_parse_mode")
    private String explanationParseMode;

    @JsonProperty("explanation_entities")
    private List<MessageEntity> explanationEntities;

    @JsonProperty("open_period")
    private Integer openPeriod;

    @JsonProperty("close_date")
    private Integer closeDate;

    @JsonProperty("is_closed")
    private Boolean isClosed;

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @JsonProperty("protect_content")
    private Boolean protectContent;

    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;

    public void setParseModeOnMarkdownV2() {
        explanationParseMode = "MarkdownV2";
    }
    public void setParseModeOnMarkdown() {
        explanationParseMode = "Markdown";
    }
    public void setParseModeOnHTML() {
        explanationParseMode = "HTML";
    }
    public void setTypeOnRegular() {
        type = "regular";
    }
    public void setTypeOnQuiz() {
        type = "quiz";
    }

    @Override
    public Message getReturnObject() {
        return new Message();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SEND_POLL.getPath();
    }

    @Override
    public void checkValidation() {
        if(chatId.isEmpty()) throw new IllegalArgumentException("Chat id cannot be empty.");

        if(type == null)
            type = "regular";

        if (!type.equals("regular") && !type.equals("quiz") && !type.equals(""))
            throw new IllegalArgumentException("Type: \"" + type + "\" does not exist.");

        if (explanationParseMode == null)
            explanationParseMode = "";

        if (!explanationParseMode.equals("HTML") && !explanationParseMode.equals("MarkdownV2") && !explanationParseMode.equals("Markdown") && !explanationParseMode.equals(""))
            throw new IllegalArgumentException("ParseMode: \"" + explanationParseMode + "\" does not exist.");

        if (!explanationParseMode.equals("") && (explanationEntities != null && !explanationEntities.isEmpty()))
            throw new IllegalArgumentException("If entities are provided, the parse mode cannot be active.");

        if(options.size() < 2 || options.size() > 10) throw new IllegalArgumentException("There has to be 2-10 options");
        for(String a : options)
            if(a.length() > 100) throw new IllegalArgumentException("Option cannot be more than 100 characters.");
    }
}
