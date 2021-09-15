package com.pawel.exceptions;

public class CannotReturnItemException extends Exception {
    public CannotReturnItemException(String errorMessage) {
        super(errorMessage);
    }
}
