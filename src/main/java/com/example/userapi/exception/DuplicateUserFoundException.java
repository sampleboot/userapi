package com.example.userapi.exception;

public class DuplicateUserFoundException extends RuntimeException {
    public DuplicateUserFoundException(String message) {
        super(message);
    }
}