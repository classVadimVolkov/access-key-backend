package com.example.accesskeybackend.exception;

public class InvalidSiteUrlException extends BaseException {
    public InvalidSiteUrlException() {
    }

    public InvalidSiteUrlException(String message) {
        super(message);
    }

    public InvalidSiteUrlException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSiteUrlException(Throwable cause) {
        super(cause);
    }

    public InvalidSiteUrlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
