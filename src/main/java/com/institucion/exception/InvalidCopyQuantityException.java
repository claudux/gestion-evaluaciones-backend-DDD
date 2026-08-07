package com.institucion.exception;

public class InvalidCopyQuantityException extends RuntimeException {
    public InvalidCopyQuantityException(String message) {
        super(message);
    }
}