package com.pawel.exceptions;

public class CannotRemovePhysicalItemException extends Exception {
    public CannotRemovePhysicalItemException(String errorMessage) {
        super(errorMessage);
    }
}
