package com.example.LeaveManagementSystem.persistence.exception;

import org.springframework.http.HttpStatus;

public class LeaveTypeNotFoundException extends CustomException {
    public LeaveTypeNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
