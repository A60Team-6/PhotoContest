package com.telerikacademy.web.photocontest.helpers;

public class UnauthorizedOperationException extends RuntimeException{

    public UnauthorizedOperationException(String message) {
        super(message);
    }

}
