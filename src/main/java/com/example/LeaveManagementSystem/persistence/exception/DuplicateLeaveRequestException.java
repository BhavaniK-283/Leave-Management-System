package com.example.LeaveManagementSystem.persistence.exception;

import org.springframework.http.HttpStatus;

public class DuplicateLeaveRequestException extends CustomException {
    public DuplicateLeaveRequestException(String message) {
      super(message, HttpStatus.BAD_REQUEST);
    }
}
