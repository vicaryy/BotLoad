package org.example.api_request;

public class GetMe implements ApiRequest {

    @Override
    public <T> T returnObject() {
        return (T) new InputFile();
    }
}
