package com.cgvsu.exceptions;

public class NullModelException extends RuntimeException {
    public NullModelException(String errorMessage) {
        super("Selected model is null: " + errorMessage);
    }

    public NullModelException() {
        super("Selected model is null");
    }
}
