package com.green.green.exceptions;

public class AuthorizationFailureException extends RuntimeException {
    public AuthorizationFailureException(String message) {
        super(message);
    }
}
