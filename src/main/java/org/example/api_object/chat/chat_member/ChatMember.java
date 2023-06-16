package org.example.api_object.chat.chat_member;

import org.example.api_object.ApiObject;
import org.example.api_object.User;

public interface ChatMember extends ApiObject {
    String getStatus();

    User getUser();
}
