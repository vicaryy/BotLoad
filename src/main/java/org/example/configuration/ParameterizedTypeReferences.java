package org.example.configuration;

import org.example.api_object.File;
import org.example.api_object.RequestResponse;
import org.example.api_object.User;
import org.example.api_object.bot.BotShortDescription;
import org.example.api_object.chat.ChatAdministratorRights;
import org.example.api_object.chat.ChatInviteLink;
import org.example.api_object.games.GameHighScore;
import org.example.api_object.inline_query.SentWebAppMessage;
import org.example.api_object.message.Message;
import org.example.api_object.other.UserProfilePhotos;
import org.example.api_object.poll.Poll;
import org.example.api_object.stickers.StickerSet;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;

@Configuration
public class ParameterizedTypeReferences {
    private final HashMap<Class, ParameterizedTypeReference> types = new HashMap<>();

    ParameterizedTypeReferences() {
        types.put(Message.class, new ParameterizedTypeReference<RequestResponse<Message>>() {
        });
        types.put(User.class, new ParameterizedTypeReference<RequestResponse<User>>() {
        });
        types.put(Boolean.class, new ParameterizedTypeReference<RequestResponse<Boolean>>() {
        });
        types.put(String.class, new ParameterizedTypeReference<RequestResponse<String>>() {
        });
        types.put(Integer.class, new ParameterizedTypeReference<RequestResponse<Integer>>() {
        });
        types.put(File.class, new ParameterizedTypeReference<RequestResponse<File>>() {
        });
        types.put(UserProfilePhotos.class, new ParameterizedTypeReference<RequestResponse<UserProfilePhotos>>() {
        });
        types.put(ChatInviteLink.class, new ParameterizedTypeReference<RequestResponse<ChatInviteLink>>() {
        });
        types.put(BotShortDescription.class, new ParameterizedTypeReference<RequestResponse<BotShortDescription>>() {
        });
        types.put(ChatAdministratorRights.class, new ParameterizedTypeReference<RequestResponse<ChatAdministratorRights>>() {
        });
        types.put(Poll.class, new ParameterizedTypeReference<RequestResponse<Poll>>() {
        });
        types.put(StickerSet.class, new ParameterizedTypeReference<RequestResponse<StickerSet>>() {
        });
        types.put(SentWebAppMessage.class, new ParameterizedTypeReference<RequestResponse<SentWebAppMessage>>() {
        });
        types.put(GameHighScore.class, new ParameterizedTypeReference<RequestResponse<GameHighScore>>() {
        });
    }

    public ParameterizedTypeReference get(Class clazz) {
        return types.get(clazz);
    }
}
