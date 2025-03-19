package com.example.LeaveManagementSystem.persistence.service;

import com.example.LeaveManagementSystem.persistence.dto.ApiResponseDto;
import com.example.LeaveManagementSystem.persistence.dto.LeaveRequestDto;
import com.example.LeaveManagementSystem.persistence.dto.LeaveResponseDto;
import com.example.LeaveManagementSystem.persistence.exception.UserNotFoundException;

import java.util.List;

public interface LeaveTransactionService {


    ApiResponseDto leaveRequest(LeaveRequestDto leaveRequestDto, String tenantId, String userId) throws UserNotFoundException;

    ApiResponseDto updateLeaveRequest(Long leaveId, LeaveRequestDto leaveRequestDto, String userId, String tenantId);

    List<LeaveResponseDto> getUserLeaveTransactions(Long userId,Long approverId);
}
