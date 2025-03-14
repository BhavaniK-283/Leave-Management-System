package com.example.LeaveManagementSystem.persistence.service.serviceImpl;

import com.example.LeaveManagementSystem.persistence.dto.LeaveRequestDto;
import com.example.LeaveManagementSystem.persistence.entity.*;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumLeaveStatus;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumStatus;
import com.example.LeaveManagementSystem.persistence.repository.*;
import com.example.LeaveManagementSystem.persistence.service.LeaveTransactionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Data
public class LeaveTransactionServiceImpl implements LeaveTransactionService {
    private final LeaveTransactionRepository leaveTransactionRepository;
    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final TenantRepository tenantRepository;
    private final ApproveWorkflowRepository approveWorkflowRepository;

    @Override
    @Transactional
    public String leaveRequest(LeaveRequestDto leaveRequestDto, String tenantId, String userId) {

        /* body
        leaveRequestDto.userId bhavani
        header userId syed
         */

        // Validate user existence using Optional
        TenantEntity tenantEntity = tenantRepository.findByIdAndStatus(Long.valueOf(tenantId),EnumStatus.ACTIVE).orElseThrow(
        ()-> new RuntimeException("Tenant not found"));

        // Validate user existence using Optional
        UserEntity requestUser = userRepository.findByIdAndStatus(Long.valueOf(userId),EnumStatus.ACTIVE).orElseThrow(
                ()-> new RuntimeException("User not found"));

        UserEntity userLeave = userRepository.findByIdAndStatus(Long.valueOf(leaveRequestDto.getUserId()),EnumStatus.ACTIVE).orElseThrow(
                ()-> new RuntimeException("User not found"));



        LeaveTypeEntity leaveTypeEntity = leaveTypeRepository.findById(Long.valueOf(leaveRequestDto.getLeaveTypeId())).orElseThrow(
                ()-> new RuntimeException("Leave Type not found"));



        LeaveTransactionEntity leaveTransaction = new LeaveTransactionEntity();
        leaveTransaction.setUser(userLeave);
        leaveTransaction.setTenant(tenantEntity);
        leaveTransaction.setLeaveType(leaveTypeEntity);
        leaveTransaction.setApprover(leaveTypeEntity.isReviewer()?userLeave.getReviewer():userLeave.getApprover());
        leaveTransaction.setStartDate(leaveRequestDto.getStartDate());
        leaveTransaction.setEndDate(leaveRequestDto.getEndDate());
        leaveTransaction.setAppliedFrom(leaveRequestDto.getAppliedFrom());
        leaveTransaction.setAppliedTo(leaveRequestDto.getAppliedTo());
        leaveTransaction.setRemarks(leaveRequestDto.getRemarks());
        leaveTransaction.setStatus(EnumStatus.ACTIVE);
        leaveTransaction.setLeaveStatus(EnumLeaveStatus.PENDING);
        leaveTransaction.setCreatedBy(requestUser.getName());
        leaveTransaction.setUpdatedBy(requestUser.getName());

        leaveTransactionRepository.save(leaveTransaction);


        // Create approval workflow for the first approver
        UserEntity approver = userLeave.getApprover();
        if (approver != null) {
            ApproveWorkflowEntity approverWorkflow = new ApproveWorkflowEntity();
            approverWorkflow.setTenant(tenantEntity);
            approverWorkflow.setLeaveTransaction(leaveTransaction);
            approverWorkflow.setApprover(approver);
            approverWorkflow.setWorkflowStatus(EnumLeaveStatus.PENDING);
            approverWorkflow.setStatus(EnumStatus.ACTIVE);
            approverWorkflow.setCreatedBy(requestUser.getName());
            approverWorkflow.setUpdatedBy(requestUser.getName());
            approveWorkflowRepository.save(approverWorkflow);
        }

        // If leave type requires a second-level reviewer, create another workflow
        if (leaveTypeEntity.isReviewer() && userLeave.getReviewer() != null) {
            UserEntity reviewer = userLeave.getReviewer();
            ApproveWorkflowEntity reviewerWorkflow = new ApproveWorkflowEntity();
            reviewerWorkflow.setTenant(tenantEntity);
            reviewerWorkflow.setLeaveTransaction(leaveTransaction);
            reviewerWorkflow.setApprover(reviewer);
            reviewerWorkflow.setWorkflowStatus(EnumLeaveStatus.PENDING);
            reviewerWorkflow.setStatus(EnumStatus.ACTIVE);
            reviewerWorkflow.setCreatedBy(requestUser.getName());
            reviewerWorkflow.setUpdatedBy(requestUser.getName());
            approveWorkflowRepository.save(reviewerWorkflow);
        }

        return "Leave request submitted successfully";
    }





}
