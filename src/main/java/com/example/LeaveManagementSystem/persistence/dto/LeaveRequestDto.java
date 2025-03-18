package com.example.LeaveManagementSystem.persistence.dto;

import com.example.LeaveManagementSystem.persistence.enumeration.EnumLeaveDuration;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LeaveRequestDto {

    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be a positive number")
    private Integer userId;
    @NotNull(message = "Leave Type ID is required")
    private Integer leaveTypeId;
    @NotNull(message = "Start Date is required")
    private LocalDate startDate;
    @NotNull(message = "End Date is required")
    private LocalDate endDate;
    @NotNull(message = "Applied From is required")
    private EnumLeaveDuration appliedFrom;
    @NotNull(message = "Applied To is required")
    private EnumLeaveDuration appliedTo;
    private String remarks;

}
