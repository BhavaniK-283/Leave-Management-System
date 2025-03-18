package com.example.LeaveManagementSystem.persistence.dto;

import com.example.LeaveManagementSystem.persistence.enumeration.EnumLeaveDuration;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumLeaveStatus;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveResponseDto {
    private long id;
    private String tenantName;
    private String userName;
    private String leaveType;
    private String approverName;
    private String remarks;

    private EnumLeaveDuration appliedFrom;
    private EnumLeaveDuration appliedTo;
    private EnumLeaveDuration approvedFor;

    private EnumLeaveStatus leaveStatus;

    private LocalDate startDate;
    private LocalDate endDate;

    private EnumStatus status;

    private String createdBy;
    private String updatedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
