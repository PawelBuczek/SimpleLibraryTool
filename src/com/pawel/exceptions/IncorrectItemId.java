package com.pawel.exceptions;

public class IncorrectItemId extends Exception{
    public IncorrectItemId(String errorMessage) {
        super(errorMessage);
    }
}
