package org.example.api_request;

import org.example.end_point.EndPoint;

public class LogOut implements ApiRequest<Boolean>{
    /**
     * Use this method to log out from the cloud Bot API server before launching the bot locally. You must log out the bot before running it locally, otherwise there is no guarantee that the bot will receive updates. After a successful call, you can immediately log in on a local server, but will not be able to log in back to the cloud Bot API server for 10 minutes. Returns True on success. Requires no parameters.
     * @return Boolean class.
     */
    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.LOG_OUT.getPath();
    }

    @Override
    public void checkValidation() {
        /**
         * Nothing to see here.
         */
    }
}
