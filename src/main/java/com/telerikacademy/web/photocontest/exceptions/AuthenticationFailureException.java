package com.telerikacademy.web.photocontest.exceptions;

public class AuthenticationFailureException extends RuntimeException {

    public AuthenticationFailureException(String message) {
        super(message);
    }
}
