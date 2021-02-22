package com.pawel.exceptions;

public class IncorrectUserId extends Exception{
    public IncorrectUserId(String errorMessage) {
        super(errorMessage);
    }
}
