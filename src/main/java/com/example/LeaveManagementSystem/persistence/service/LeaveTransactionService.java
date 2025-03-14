package com.example.LeaveManagementSystem.persistence.service;

import com.example.LeaveManagementSystem.persistence.dto.LeaveRequestDto;

public interface LeaveTransactionService {
    String leaveRequest(LeaveRequestDto leaveRequestDto, String tenantId, String userId);
}
