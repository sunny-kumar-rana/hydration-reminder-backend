package com.hydration.exception;

import com.hydration.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExists(UsernameAlreadyExistsException exception){
        ErrorResponse response = new ErrorResponse();
        response.setMessage(exception.getMessage());
        response.setStatus(HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException exception){
        ErrorResponse response = new ErrorResponse();
        response.setMessage(exception.getMessage());
        response.setStatus(HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
