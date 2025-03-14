package com.example.LeaveManagementSystem.persistence.controller;


import com.example.LeaveManagementSystem.persistence.dto.LeaveRequestDto;
import com.example.LeaveManagementSystem.persistence.service.LeaveTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lms")
@RequiredArgsConstructor

public class LeaveTransactionController {
    @Autowired
    private LeaveTransactionService leaveTransactionService;

    @PostMapping("/leave_request")
    public ResponseEntity<String> applyLeave(@RequestBody LeaveRequestDto leaveRequestDto, @RequestHeader String tenantId,@RequestHeader String userId) {
        String response = leaveTransactionService.leaveRequest(leaveRequestDto,tenantId,userId);
        return ResponseEntity.ok(response);
    }
}

