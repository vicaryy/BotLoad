package org.example.configuration;

import org.example.api_object.File;
import org.example.api_object.RequestResponse;
import org.example.api_object.User;
import org.example.api_object.chat.ChatInviteLink;
import org.example.api_object.message.Message;
import org.example.api_object.other.UserProfilePhotos;
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
        types.put(File.class, new ParameterizedTypeReference<RequestResponse<File>>() {
        });
        types.put(UserProfilePhotos.class, new ParameterizedTypeReference<RequestResponse<UserProfilePhotos>>() {
        });
        types.put(ChatInviteLink.class, new ParameterizedTypeReference<RequestResponse<ChatInviteLink>>() {
        });
    }

    public ParameterizedTypeReference get(Class clazz) {
        return types.get(clazz);
    }
}
