package org.vicary.api_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerCallbackQuery implements ApiRequest<Boolean> {
    /**
     * Use this method to send answers to callback queries sent from inline keyboards.
     * The answer will be displayed to the user as a notification at the top of the chat screen or as an alert.
     * On success, True is returned.
     * <p>
     * Alternatively, the user can be redirected to the specified Game URL.
     * For this option to work, you must first create a game for your bot via @BotFather and accept the terms.
     * Otherwise, you may use links like t.me/your_bot?start=XXXX that open your bot with a parameter.
     *
     * @param callbackQueryId Unique identifier for the query to be answered
     * @param text            Text of the notification. If not specified, nothing will be shown to the user, 0-200 characters
     * @param showAlert       If True, an alert will be shown by the client instead of a notification at the top of the chat screen. Defaults to false.
     * @param url             URL that will be opened by the user's client. If you have created a Game and accepted the conditions via @BotFather,
     * specify the URL that opens your game - note that this will only work if the query comes from a callback_game button.
     * Otherwise, you may use links like t.me/your_bot?start=XXXX that open your bot with a parameter.
     * @param cacheTime       The maximum amount of time in seconds that the result of the callback query may be cached client-side.
     * Telegram apps will support caching starting in version 3.14. Defaults to 0.
     */
    @NonNull
    @JsonProperty("callback_query_id")
    private String callbackQueryId;

    @JsonProperty("text")
    private String text;

    @JsonProperty("show_alert")
    private Boolean showAlert;

    @JsonProperty("url")
    private String url;

    @JsonProperty("cache_time")
    private Integer cacheTime;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.ANSWER_CALLBACK_QUERY.getPath();
    }

    @Override
    public void checkValidation() {
        if (callbackQueryId.isEmpty()) throw new IllegalArgumentException("callbackQueryId cannot be empty.");
    }
}
