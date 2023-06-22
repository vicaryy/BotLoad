package org.example.api_request;

public interface ApiRequest<T> extends Validation {
    T getReturnObject();

    public String getEndPoint();
}
