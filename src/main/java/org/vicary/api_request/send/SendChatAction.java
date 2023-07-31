package org.vicary.api_request.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SendChatAction implements ApiRequest<Boolean> {
    /**
     * Use this method when you need to tell the user that something is happening on the bot's side.
     * The status is set for 5 seconds or less (when a message arrives from your bot, Telegram clients clear its typing status).
     * Returns True on success.
     *
     * @param chatId                Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId       Unique identifier for the target message thread; supergroups only.
     * @param action                Type of action to broadcast.
     * Choose one, depending on what the user is about to receive:
     * - "typing" for text messages,
     * - "upload_photo" for photos,
     * - "record_video" or "upload_video" for videos,
     * - "record_voice" or "upload_voice" for voice notes,
     * - "upload_document" for general files,
     * - "choose_sticker" for stickers,
     * - "find_location" for location data,
     * - "record_video_note" or "upload_video_note" for video notes.
     */

    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @NonNull
    @JsonProperty("action")
    private String action;

    public void setActionOnTyping() {
        this.action = "typing";
    }

    public void setActionOnUploadPhoto() {
        this.action = "upload_photo";
    }

    public void setActionOnRecordVideo() {
        this.action = "record_video";
    }

    public void setActionOnRecordVoice() {
        this.action = "record_voice";
    }

    public void setActionOnUploadDocument() {
        this.action = "upload_document";
    }

    public void setActionOnChooseSticker() {
        this.action = "choose_sticker";
    }

    public void setActionOnFindLocation() {
        this.action = "find_location";
    }

    public void setActionOnRecordVideoNote() {
        this.action = "record_video_note";
    }

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SEND_CHAT_ACTION.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("Chat id cannot be empty.");

        if (!action.equals("typing")
                && !action.equals("upload_photo")
                && !action.equals("record_video")
                && !action.equals("upload_document")
                && !action.equals("choose_sticker")
                && !action.equals("find_location")
                && !action.equals("record_video_note"))
            throw new IllegalArgumentException("Action: \"" + action + "\" does not exist.");
    }
}
