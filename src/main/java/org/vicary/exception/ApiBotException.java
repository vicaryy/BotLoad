package org.vicary.exception;

import lombok.Getter;

@Getter
public class ApiBotException extends RuntimeException {
    private String loggerMessage;

    public ApiBotException(String s, String loggerMessage) {
        super(s);
        this.loggerMessage = loggerMessage;
    }

    public ApiBotException() {
        super();
    }
}
