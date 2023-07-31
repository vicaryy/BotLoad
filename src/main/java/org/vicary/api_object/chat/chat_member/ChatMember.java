package org.vicary.api_object.chat.chat_member;

import org.vicary.api_object.ApiObject;
import org.vicary.api_object.User;

public interface ChatMember extends ApiObject {
    String getStatus();

    User getUser();
}
