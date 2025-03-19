package com.example.LeaveManagementSystem.persistence.controller;


import com.example.LeaveManagementSystem.persistence.dto.ApiResponseDto;
import com.example.LeaveManagementSystem.persistence.dto.LeaveRequestDto;
import com.example.LeaveManagementSystem.persistence.dto.LeaveResponseDto;
import com.example.LeaveManagementSystem.persistence.exception.UserNotFoundException;
import com.example.LeaveManagementSystem.persistence.service.LeaveTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lms")
@RequiredArgsConstructor

public class LeaveTransactionController {

    @Autowired
    private final LeaveTransactionService leaveTransactionService;

    @Operation(
            summary = "Apply for leave",
            description = "Submit a leave request"
    )
    @ApiResponse(responseCode = "200", description = "Leave request submitted successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")

    @PostMapping("/leave_request")
    public ApiResponseDto applyLeave(@Valid @RequestBody LeaveRequestDto leaveRequestDto, @RequestHeader String tenantId, @RequestHeader String userId) throws UserNotFoundException {

        return leaveTransactionService.leaveRequest(leaveRequestDto, tenantId, userId);
    }


    @PutMapping("/update/{leaveId}")
    public ApiResponseDto updateLeaveRequest(
            @PathVariable Long leaveId,
            @RequestBody LeaveRequestDto leaveRequestDto,
            @RequestHeader String userId,
            @RequestHeader String tenantId) {

        return leaveTransactionService.updateLeaveRequest(leaveId, leaveRequestDto, userId, tenantId);
    }




    @GetMapping("/getLeaveReq")
    public List<LeaveResponseDto> getUserLeaveTransactions(
            @RequestHeader(value = "userId", required = false) Long userId,
            @RequestHeader(value = "approverId", required = false) Long approverId) {

        return leaveTransactionService.getUserLeaveTransactions(userId, approverId);
    }

}

