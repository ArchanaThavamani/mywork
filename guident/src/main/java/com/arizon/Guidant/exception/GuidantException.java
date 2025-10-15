package com.arizon.Guidant.exception;

public class GuidantException extends Exception {

    private static final long serialVersionUID = 1L;

    public GuidantException(String message) {
        super(message);
    }

    public GuidantException(Throwable cause) {
        super(cause);
    }

    public GuidantException(String message, Throwable cause) {
        super(message, cause);
    }
}
