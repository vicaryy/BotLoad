package org.example.configuration;

import org.example.api_object.RequestResponse;
import org.example.api_object.User;
import org.example.api_object.message.Message;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;

@Configuration
public class ParameterizedTypeReferences {
    private final HashMap<Class, ParameterizedTypeReference> TYPES = new HashMap<>();

    ParameterizedTypeReferences() {
        TYPES.put(Message.class, new ParameterizedTypeReference<RequestResponse<Message>>() {
        });
        TYPES.put(User.class, new ParameterizedTypeReference<RequestResponse<User>>() {
        });
        TYPES.put(Boolean.class, new ParameterizedTypeReference<RequestResponse<Boolean>>() {
        });
    }

    public ParameterizedTypeReference get(Class clazz) {
        return TYPES.get(clazz);
    }
}
