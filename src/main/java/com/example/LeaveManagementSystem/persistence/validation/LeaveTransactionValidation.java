package com.example.LeaveManagementSystem.persistence.validation;


import com.example.LeaveManagementSystem.persistence.dto.LeaveRequestDto;
import com.example.LeaveManagementSystem.persistence.entity.LeaveTransactionEntity;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumLeaveDuration;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumLeaveStatus;
import com.example.LeaveManagementSystem.persistence.exception.*;
import com.example.LeaveManagementSystem.persistence.entity.LeaveTypeEntity;
import com.example.LeaveManagementSystem.persistence.entity.TenantEntity;
import com.example.LeaveManagementSystem.persistence.entity.UserEntity;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumStatus;
import com.example.LeaveManagementSystem.persistence.repository.LeaveTransactionRepository;
import com.example.LeaveManagementSystem.persistence.repository.LeaveTypeRepository;
import com.example.LeaveManagementSystem.persistence.repository.TenantRepository;
import com.example.LeaveManagementSystem.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LeaveTransactionValidation {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveTransactionRepository leaveTransactionRepository;



    public TenantEntity validateTenant(String tenantId) {
        return tenantRepository.findByIdAndStatus(Long.valueOf(tenantId), EnumStatus.ACTIVE)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found"));
    }
    public UserEntity validateUser(String userId, Long tenantId) throws UserNotFoundException {
        return userRepository.findByIdAndStatusAndTenantId(Long.valueOf(userId), EnumStatus.ACTIVE, tenantId)
                .orElseThrow(() -> new UserNotFoundException("User not found", HttpStatus.BAD_REQUEST.value()));
    }
    public LeaveTypeEntity validateLeaveType(String leaveTypeId) {
        return leaveTypeRepository.findById(Long.valueOf(leaveTypeId))
                .orElseThrow(() -> new LeaveTypeNotFoundException("Leave Type not found."));
    }
    public void validateLeaveDates(LocalDate startDate, LocalDate endDate) throws InvalidLeaveDateException {
        LocalDate today = LocalDate.now();

        if (startDate.isBefore(today)) {
            throw new InvalidLeaveDateException("Leave start date cannot be in the past", HttpStatus.BAD_REQUEST.value());
        }

        if (endDate.isBefore(startDate)) {
            throw new InvalidLeaveDateException("End date cannot be before start date", HttpStatus.BAD_REQUEST.value());
        }
    }


    public void checkDuplicateLeaveRequest(UserEntity user, LeaveRequestDto leaveRequestDto) throws DuplicateLeaveRequestException {

        List<LeaveTransactionEntity> existingLeaves = leaveTransactionRepository.findByUserAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndLeaveStatusIn(
                user,
                leaveRequestDto.getEndDate(),
                leaveRequestDto.getStartDate(),
                List.of(EnumLeaveStatus.PENDING, EnumLeaveStatus.APPROVED)
        );

        for (LeaveTransactionEntity existingLeave : existingLeaves) {
            boolean isOverlap = false;

            // Case 1: Full overlap (same start and end date, conflicting leave durations)
            if (leaveRequestDto.getStartDate().equals(existingLeave.getStartDate()) &&
                    leaveRequestDto.getEndDate().equals(existingLeave.getEndDate())) {
                isOverlap = true;
            }

            // Case 2: Overlapping dates but **valid half-day transitions**
            if (leaveRequestDto.getStartDate().equals(existingLeave.getEndDate()) &&
                    leaveRequestDto.getAppliedFrom() == EnumLeaveDuration.FULL_DAY &&
                    existingLeave.getAppliedTo() == EnumLeaveDuration.FIRST_HALF) {
                isOverlap = false; // Valid transition (New leave starts after the existing leave ends)
            }
            // Valid transition (Existing leave starts after new leave ends)
            if (leaveRequestDto.getEndDate().equals(existingLeave.getStartDate()) &&
                    leaveRequestDto.getAppliedTo() == EnumLeaveDuration.FULL_DAY &&
                    existingLeave.getAppliedFrom() == EnumLeaveDuration.SECOND_HALF) {
                isOverlap = false;
            }

            // Prevent improper half-day overlaps**
            if (leaveRequestDto.getStartDate().equals(existingLeave.getStartDate()) ||
                    leaveRequestDto.getEndDate().equals(existingLeave.getEndDate())) {

                if ((leaveRequestDto.getAppliedFrom() == EnumLeaveDuration.SECOND_HALF &&
                        existingLeave.getAppliedTo() == EnumLeaveDuration.FIRST_HALF) ||
                        (leaveRequestDto.getAppliedTo() == EnumLeaveDuration.FIRST_HALF &&
                                existingLeave.getAppliedFrom() == EnumLeaveDuration.SECOND_HALF)) {
                    isOverlap = false; // Valid continuation
                } else {
                    isOverlap = true; // Conflicting half-day leaves
                }
            }

            if (isOverlap) {
                throw new DuplicateLeaveRequestException("Leave Request overlaps with an existing leave.");
            }
        }
    }


}
