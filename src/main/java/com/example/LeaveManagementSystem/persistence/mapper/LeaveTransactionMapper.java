package com.example.LeaveManagementSystem.persistence.mapper;

import com.example.LeaveManagementSystem.persistence.dto.LeaveRequestDto;
import com.example.LeaveManagementSystem.persistence.dto.LeaveResponseDto;
import com.example.LeaveManagementSystem.persistence.entity.*;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumLeaveStatus;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class LeaveTransactionMapper {

    public LeaveTransactionEntity leaveRequestDtoToLeaveTransactionEntity(LeaveRequestDto leaveRequestDto, TenantEntity tenantEntity, UserEntity requestUser, UserEntity userLeave, LeaveTypeEntity leaveTypeEntity, LocalDate startDate, LocalDate endDate) {
        LeaveTransactionEntity leaveTransaction = new LeaveTransactionEntity();
        leaveTransaction.setUser(userLeave);
        leaveTransaction.setTenant(tenantEntity);
        leaveTransaction.setLeaveType(leaveTypeEntity);
        leaveTransaction.setApprover(leaveTypeEntity.isReviewer() && !leaveTypeEntity.isApprover() ? userLeave.getReviewer() : userLeave.getApprover());
        leaveTransaction.setStartDate(startDate);
        leaveTransaction.setEndDate(endDate);
        leaveTransaction.setAppliedFrom(leaveRequestDto.getAppliedFrom());
        leaveTransaction.setAppliedTo(leaveRequestDto.getAppliedTo());
        leaveTransaction.setRemarks(leaveRequestDto.getRemarks());
        leaveTransaction.setStatus(EnumStatus.ACTIVE);
        leaveTransaction.setLeaveStatus(EnumLeaveStatus.PENDING);
        leaveTransaction.setCreatedBy(requestUser.getName());
        leaveTransaction.setUpdatedBy(requestUser.getName());
        return leaveTransaction;
    }


    public List<ApproveWorkflowEntity> leaveRequestDtoToApproveWorkflowEntities(LeaveTransactionEntity leaveTransaction, TenantEntity tenantEntity, UserEntity requestUser, UserEntity userLeave) {
        List<ApproveWorkflowEntity> approveWorkflowEntities = new ArrayList<>();

        if (userLeave.getApprover() != null) {
            ApproveWorkflowEntity approverWorkflow = new ApproveWorkflowEntity();
            approverWorkflow.setTenant(tenantEntity);
            approverWorkflow.setLeaveTransaction(leaveTransaction);
            approverWorkflow.setApprover(userLeave.getApprover());
            approverWorkflow.setWorkflowStatus(EnumLeaveStatus.PENDING);
            approverWorkflow.setStatus(EnumStatus.ACTIVE);
            approverWorkflow.setCreatedBy(requestUser.getName());
            approverWorkflow.setUpdatedBy(requestUser.getName());
            approveWorkflowEntities.add(approverWorkflow);
        }

        if (userLeave.getReviewer() != null) {
            ApproveWorkflowEntity reviewerWorkflow = new ApproveWorkflowEntity();
            reviewerWorkflow.setTenant(tenantEntity);
            reviewerWorkflow.setLeaveTransaction(leaveTransaction);
            reviewerWorkflow.setApprover(userLeave.getReviewer());
            reviewerWorkflow.setWorkflowStatus(EnumLeaveStatus.PENDING);
            reviewerWorkflow.setStatus(EnumStatus.ACTIVE);
            reviewerWorkflow.setCreatedBy(requestUser.getName());
            reviewerWorkflow.setUpdatedBy(requestUser.getName());
            approveWorkflowEntities.add(reviewerWorkflow);
        }

        return approveWorkflowEntities;
    }

    public LeaveTransactionEntity leaveUpadteDtoToLeaveTransactionEntity(LeaveRequestDto leaveRequestDto, UserEntity requestUser, LeaveTransactionEntity userLeave, LocalDate startDate, LocalDate endDate) {
        // Update leave request details
        userLeave.setStartDate(startDate);
        userLeave.setEndDate(endDate);
        userLeave.setAppliedFrom(leaveRequestDto.getAppliedFrom());
        userLeave.setAppliedTo(leaveRequestDto.getAppliedTo());
        userLeave.setRemarks(leaveRequestDto.getRemarks());
        userLeave.setUpdatedBy(requestUser.getName());

        return userLeave;
    }

    public LeaveRequestDto leaveTransactionEntityToDto(LeaveTransactionEntity entity) {
        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setUserId((int) entity.getUser().getId());

        dto.setLeaveTypeId((int) entity.getLeaveType().getId());
        dto.setStartDate(String.valueOf(entity.getStartDate()) );
        dto.setEndDate(String.valueOf(entity.getEndDate()));
        dto.setAppliedFrom(entity.getAppliedFrom());
        dto.setAppliedTo(entity.getAppliedTo());
        dto.setRemarks(entity.getRemarks());
        return dto;
    }

    public LeaveResponseDto leaveTransactionEntityToResponseDto(LeaveTransactionEntity entity) {
        LeaveResponseDto dto = new LeaveResponseDto();

        dto.setId(entity.getId());
        dto.setTenantName(entity.getUser().getTenant().getName());
        dto.setUserName(entity.getUser().getName());
        dto.setLeaveType(entity.getLeaveType().getName());
        dto.setApproverId(entity.getApprover() != null ? entity.getApprover().getId() : null);
        dto.setApproverName(entity.getApprover() != null ? entity.getApprover().getName() : "N/A");
        dto.setRemarks(entity.getRemarks());
        dto.setApprovedFor(entity.getApprovedFor());
        dto.setLeaveStatus(entity.getLeaveStatus());
        dto.setStartDate(entity.getStartDate());
        dto.setAppliedFrom(entity.getAppliedFrom());
        dto.setEndDate(entity.getEndDate());
        dto.setAppliedTo(entity.getAppliedTo());
        dto.setStatus(entity.getStatus());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }




}
