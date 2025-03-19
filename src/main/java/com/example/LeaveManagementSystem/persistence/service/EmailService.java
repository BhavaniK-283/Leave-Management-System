package com.example.LeaveManagementSystem.persistence.service;

import com.example.LeaveManagementSystem.persistence.entity.UserEntity;

public interface EmailService {
    void sendLeaveAppliedEmail(UserEntity userId, String startDate, String endDate);
}
