package org.vicary.exception;

public class DownloadedFileNotFoundException extends ApiBotException {
    public DownloadedFileNotFoundException(String s, String loggerMessage) {
        super(s, loggerMessage);
    }
}
