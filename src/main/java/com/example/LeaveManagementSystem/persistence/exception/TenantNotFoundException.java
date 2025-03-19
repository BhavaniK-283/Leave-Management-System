package com.example.LeaveManagementSystem.persistence.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TenantNotFoundException extends RuntimeException {
    public TenantNotFoundException(String message) {
        super(message);

    }
}
