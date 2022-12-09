package com.fiserv.exceptions;

public class SessionException extends RuntimeException {

    public SessionException(final String errorMessage) {
        super(errorMessage);
    }
}
