package com.pawel.exceptions;

public class CannotBorrowItemException extends Exception {
    public CannotBorrowItemException(String errorMessage) {
        super(errorMessage);
    }
}
