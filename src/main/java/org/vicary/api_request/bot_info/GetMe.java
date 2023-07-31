package org.vicary.api_request.bot_info;

import lombok.Data;
import org.vicary.api_object.User;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
public class GetMe implements ApiRequest<User> {
    /**
     * A simple method for testing your bot's authentication token. Requires no parameters. Returns basic information about the bot in form of a User object.
     * @return User class.
     */
    @Override
    public User getReturnObject() {
        return new User();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_ME.getPath();
    }

    @Override
    public void checkValidation() {
        /**
         * Nothing to see here.
         */
    }
}
