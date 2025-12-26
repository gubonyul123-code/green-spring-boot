package com.green.green.exceptions;

public class AuthenticationFailureException extends RuntimeException {
    public AuthenticationFailureException(String message) {
        super(message);
    }
}
