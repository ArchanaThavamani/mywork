package com.arizon.productcommon.exception;

public class PCProductFrequentException extends Exception {

    private static final long serialVersionUID = 1;

    public PCProductFrequentException(String message) {
        super(message);
    }

    public PCProductFrequentException(Throwable cause) {
        super(cause);
    }


    public PCProductFrequentException(String message, Throwable cause) {
        super(message, cause);
    }
}




