package org.vicary.exception;

public class BotSendException extends Exception {
    public BotSendException(String message) {
        super(message);
    }

    public BotSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
