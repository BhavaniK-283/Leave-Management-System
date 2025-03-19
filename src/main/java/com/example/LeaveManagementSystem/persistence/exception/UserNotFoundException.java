package com.example.LeaveManagementSystem.persistence.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends CustomException {


    public UserNotFoundException(String message) {
        super(message);

    }
}
