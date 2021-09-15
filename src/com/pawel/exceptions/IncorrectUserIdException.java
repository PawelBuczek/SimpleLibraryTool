package com.pawel.exceptions;

public class IncorrectUserIdException extends Exception {
    public IncorrectUserIdException(String errorMessage) {
        super(errorMessage);
    }
}
