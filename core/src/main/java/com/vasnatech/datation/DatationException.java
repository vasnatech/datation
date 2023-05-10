package com.vasnatech.datation;

public class DatationException extends RuntimeException {

    public DatationException() {
    }

    public DatationException(String message) {
        super(message);
    }

    public DatationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatationException(Throwable cause) {
        super(cause);
    }

    public DatationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
