package org.example.configuration;

import org.example.api_object.File;
import org.example.api_object.RequestResponse;
import org.example.api_object.User;
import org.example.api_object.message.Message;
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
        types.put(File.class, new ParameterizedTypeReference<RequestResponse<File>>() {
        });
    }

    public ParameterizedTypeReference get(Class clazz) {
        return types.get(clazz);
    }
}
