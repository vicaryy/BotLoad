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
    SEND_MEDIA_GROUP("/sendMediaGroup"),
    SEND_VENUE("/sendVenue"),
    SEND_CONTACT("/sendContact"),
    SEND_POLL("/sendPoll"),
    SEND_DICE("/sendDice"),
    SEND_CHAT_ACTION("/sendChatAction"),
    GET_USER_PROFILE_PHOTOS("/getUserProfilePhotos"),
    BAN_CHAT_MEMBER("/banChatMember"),
    UNBAN_CHAT_MEMBER("/unbanChatMember"),
    RESTRICT_CHAT_MEMBER("/restrictChatMember"),
    PROMOTE_CHAT_MEMBER("/promoteChatMember"),
    SET_CHAT_ADMINISTRATOR_CUSTOM_TITLE("/setChatAdministratorCustomTitle"),
    BAN_CHAT_SENDER_CHAT("/banChatSenderChat"),
    UNBAN_CHAT_SENDER_CHAT("/unbanChatSenderChat"),
    SET_CHAT_PERMISSIONS("/setChatPermissions"),
    EXPORT_CHAT_INVITE_LINK("/exportChatInviteLink"),
    CREATE_CHAT_INVITE_LINK("/createChatInviteLink"),
    EDIT_CHAT_INVITE_LINK("/editChatInviteLink"),
    REVOKE_CHAT_INVITE_LINK("/revokeChatInviteLink"),
    APPROVE_CHAT_JOIN_REQUEST("/approveChatJoinRequest"),
    SEND_LOCATION("/sendLocation");

    private final String path;

    EndPoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
