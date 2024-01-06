package com.cgvsu.exceptions;

import java.io.IOException;

public class IncorrectInputException extends IOException {
    public IncorrectInputException(String errorMessage) {
        super("Incorrect input: " + errorMessage);
    }
}
