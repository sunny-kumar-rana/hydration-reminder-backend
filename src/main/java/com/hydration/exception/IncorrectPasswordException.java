package com.hydration.exception;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException() {
        super("Current password is incorrect.");
    }
}