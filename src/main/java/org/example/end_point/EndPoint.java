package org.example.end_point;

public enum EndPoint {
    GET_UPDATES("/getUpdates?offset="),
    GET_ME("/getMe"),
    LOG_OUT("/logOut"),
    CLOSE("/close"),
    SEND_MESSAGE("/sendMessage"),
    FORWARD_MESSAGES("/forwardMessage"),
    COPY_MESSAGE("/copyMessage"),
    SEND_PHOTO("/sendPhoto"),
    SEND_AUDIO("/sendAudio"),
    SEND_DOCUMENT("/sendDocument"),
    SEND_VIDEO("/sendVideo"),
    SEND_ANIMATION("/sendAnimation"),
    SEND_VOICE("/sendVoice");

    private final String path;

    EndPoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
