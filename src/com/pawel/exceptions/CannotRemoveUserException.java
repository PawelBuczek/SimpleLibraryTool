package com.pawel.exceptions;

public class CannotRemoveUserException extends Exception {
    public CannotRemoveUserException(String errorMessage) {
        super(errorMessage);
    }
}
