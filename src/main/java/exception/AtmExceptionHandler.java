package com.atm.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AtmExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex) {
        return "Error: " + ex.getMessage();
    }
}