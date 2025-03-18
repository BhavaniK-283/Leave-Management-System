package com.example.LeaveManagementSystem.persistence.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data

public class ApiResponseDto {
    private int status;
    private String message;

    public ApiResponseDto(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
