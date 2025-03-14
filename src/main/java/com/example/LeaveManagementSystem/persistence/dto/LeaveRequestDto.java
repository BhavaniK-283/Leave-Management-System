package com.example.LeaveManagementSystem.persistence.dto;

import com.example.LeaveManagementSystem.persistence.enumeration.EnumLeaveDuration;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestDto {
    private Integer userId;
    private Integer leaveTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private EnumLeaveDuration appliedFrom;
    private EnumLeaveDuration appliedTo; // FIRST_HALF, SECOND_HALF, FULL_DAY
    // FIRST_HALF, SECOND_HALF, FULL_DAY
    private String remarks;
}
