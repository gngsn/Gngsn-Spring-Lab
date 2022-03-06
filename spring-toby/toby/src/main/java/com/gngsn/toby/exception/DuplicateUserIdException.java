package com.gngsn.toby.exception;

public class DuplicateUserIdException extends RuntimeException {
    public DuplicateUserIdException() {
        super();
    }

    public DuplicateUserIdException(Throwable cause) {
        super(cause);
    }
}
