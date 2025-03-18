package com.example.LeaveManagementSystem.persistence.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends Exception {
    private final int httpCode;

    public UserNotFoundException(String message, int httpCode) {
        super(message);
        this.httpCode = httpCode;
    }
}
