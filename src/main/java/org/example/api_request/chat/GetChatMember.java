package org.example.api_request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.User;
import org.example.api_object.chat.chat_member.ChatMember;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class GetChatMember implements ApiRequest<ChatMember> {
    /**
     * Use this method to get information about a member of a chat.
     * The method is only guaranteed to work for other users if the bot is an administrator in the chat.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
     * @param userId Unique identifier of the target user
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("user_id")
    private Integer userId;

    @Override
    public ChatMember getReturnObject() {
        return new ChatMember() {
            @Override
            public String getStatus() {
                return null;
            }

            @Override
            public User getUser() {
                return null;
            }
        };

        //TO-DO
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_CHAT_MEMBER.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
