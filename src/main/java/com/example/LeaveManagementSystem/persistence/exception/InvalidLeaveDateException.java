package com.example.LeaveManagementSystem.persistence.exception;

import org.springframework.http.HttpStatus;

public class InvalidLeaveDateException extends CustomException {
    public InvalidLeaveDateException(String message, int value) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
