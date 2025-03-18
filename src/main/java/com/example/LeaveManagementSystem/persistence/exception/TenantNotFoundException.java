package com.example.LeaveManagementSystem.persistence.exception;

import org.springframework.http.HttpStatus;


public class TenantNotFoundException extends CustomException {
    public TenantNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
