package org.example.end_point;

public enum EndPoint {
    GET_UPDATES("/getUpdates?offset="),
    GET_ME("/getMe"),
    GET_FILE("/getFile"),
    LOG_OUT("/logOut"),
    CLOSE("/close"),
    SEND_MESSAGE("/sendMessage"),
    FORWARD_MESSAGE("/forwardMessage"),
    COPY_MESSAGE("/copyMessage"),
    SEND_PHOTO("/sendPhoto"),
    SEND_AUDIO("/sendAudio"),
    SEND_DOCUMENT("/sendDocument"),
    SEND_VIDEO("/sendVideo"),
    SEND_ANIMATION("/sendAnimation"),
    SEND_VOICE("/sendVoice"),
    SEND_VIDEO_NOTE("/sendVideoNote"),
    SEND_MEDIA_GROUP("/sendMediaGroup");

    private final String path;

    EndPoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
