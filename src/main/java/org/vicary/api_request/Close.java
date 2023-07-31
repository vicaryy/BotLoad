package org.vicary.api_request;

import org.vicary.end_point.EndPoint;

public class Close implements ApiRequest<Boolean> {
    /**
     * Use this method to close the bot instance before moving it from one local server to another. You need to delete the webhook before calling this method to ensure that the bot isn't launched again after server restart. The method will return error 429 in the first 10 minutes after the bot is launched. Returns True on success. Requires no parameters.
     * @return Boolean class.
     */
    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.CLOSE.getPath();
    }

    @Override
    public void checkValidation() {
        /**
         * Nothing to see here.
         */
    }
}
