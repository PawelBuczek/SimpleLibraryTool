package com.pawel.exceptions;

public class CannotRemoveVirtualItemException extends Exception {
    public CannotRemoveVirtualItemException(String errorMessage) {
        super(errorMessage);
    }
}
