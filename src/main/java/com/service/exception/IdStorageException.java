package com.service.exception;

public class IdStorageException extends RuntimeException {
    public IdStorageException(String message) {
        super(message);
    }

    public IdStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
