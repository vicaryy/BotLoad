package org.vicary.exception;

public class InvalidBotRequestException extends ApiBotException {
    public InvalidBotRequestException(String s, String loggerMessage) {
        super(s, loggerMessage);
    }
}
