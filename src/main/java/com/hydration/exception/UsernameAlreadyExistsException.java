package com.hydration.exception;

public class UsernameAlreadyExistsException extends RuntimeException{

    public UsernameAlreadyExistsException(){
        super("Username already exists");
    }
}
