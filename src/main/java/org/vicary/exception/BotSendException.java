package org.vicary.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.nio.charset.Charset;

public class BotSendException extends WebClientResponseException {
    public BotSendException(int statusCode, String statusText, HttpHeaders headers, byte[] body, Charset charset) {
        super(statusCode, statusText, headers, body, charset);
    }

    public BotSendException(int status, String reasonPhrase, HttpHeaders headers, byte[] body, Charset charset, HttpRequest request) {
        super(status, reasonPhrase, headers, body, charset, request);
    }

    public BotSendException(HttpStatusCode statusCode, String reasonPhrase, HttpHeaders headers, byte[] body, Charset charset, HttpRequest request) {
        super(statusCode, reasonPhrase, headers, body, charset, request);
    }

    public BotSendException(String message, int statusCode, String statusText, HttpHeaders headers, byte[] responseBody, Charset charset) {
        super(message, statusCode, statusText, headers, responseBody, charset);
    }

    public BotSendException(String message, int statusCode, String statusText, HttpHeaders headers, byte[] responseBody, Charset charset, HttpRequest request) {
        super(message, statusCode, statusText, headers, responseBody, charset, request);
    }

    public BotSendException(String message, HttpStatusCode statusCode, String statusText, HttpHeaders headers, byte[] responseBody, Charset charset, HttpRequest request) {
        super(message, statusCode, statusText, headers, responseBody, charset, request);
    }
}
