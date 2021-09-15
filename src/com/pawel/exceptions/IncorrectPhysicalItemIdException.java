package com.pawel.exceptions;

public class IncorrectPhysicalItemIdException extends Exception {
    public IncorrectPhysicalItemIdException(String errorMessage) {
        super(errorMessage);
    }
}
