package com.example.LeaveManagementSystem.persistence.service.serviceImpl;

import com.example.LeaveManagementSystem.persistence.constants.ResponseConstant;
import com.example.LeaveManagementSystem.persistence.dto.ApiResponseDto;
import com.example.LeaveManagementSystem.persistence.dto.LeaveRequestDto;
import com.example.LeaveManagementSystem.persistence.entity.*;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumLeaveStatus;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumRoleType;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumStatus;
import com.example.LeaveManagementSystem.persistence.exception.UserNotFoundException;
import com.example.LeaveManagementSystem.persistence.mapper.LeaveTransactionMapper;
import com.example.LeaveManagementSystem.persistence.repository.*;
import com.example.LeaveManagementSystem.persistence.service.EmailService;
import com.example.LeaveManagementSystem.persistence.service.LeaveTransactionService;
import com.example.LeaveManagementSystem.persistence.validation.LeaveTransactionValidation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Data
public class LeaveTransactionServiceImpl implements LeaveTransactionService {
    private final LeaveTransactionRepository leaveTransactionRepository;
    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final TenantRepository tenantRepository;
    private final ApproveWorkflowRepository approveWorkflowRepository;
    private final LeaveTransactionMapper leaveTransactionMapper;
    private final LeaveTransactionValidation leaveTransactionValidation;
    private final EmailService emailService;

    @Override


    public ApiResponseDto leaveRequest(LeaveRequestDto leaveRequestDto, String tenantId, String userId) throws UserNotFoundException {

        // Validate Tenant
        TenantEntity tenantEntity = leaveTransactionValidation.validateTenant(tenantId);

        UserEntity requestUser = leaveTransactionValidation.validateUser(userId, tenantEntity.getId());

        UserEntity userLeave = leaveTransactionValidation.validateUser(String.valueOf(leaveRequestDto.getUserId()), tenantEntity.getId());

        LeaveTypeEntity leaveTypeEntity = leaveTransactionValidation.validateLeaveType(String.valueOf(leaveRequestDto.getLeaveTypeId()));

        leaveTransactionValidation.validateLeaveDates(leaveRequestDto.getStartDate(), leaveRequestDto.getEndDate());

        leaveTransactionValidation.checkDuplicateLeaveRequest(userLeave, leaveRequestDto);

        LeaveTransactionEntity leaveTransaction = leaveTransactionMapper.leaveRequestDtoToLeaveTransactionEntity(leaveRequestDto, tenantEntity, requestUser, userLeave, leaveTypeEntity);
        leaveTransactionRepository.save(leaveTransaction);


        List<ApproveWorkflowEntity> approveWorkflowEntities = leaveTransactionMapper.leaveRequestDtoToApproveWorkflowEntities(leaveTransaction, tenantEntity, requestUser, userLeave);
        approveWorkflowRepository.saveAll(approveWorkflowEntities);

        //send mail
        emailService.sendLeaveAppliedEmail(userLeave,String.valueOf(leaveRequestDto.getStartDate()), String.valueOf(leaveRequestDto.getEndDate()));

        return new ApiResponseDto(HttpStatus.OK.value(), ResponseConstant.LEAVE_CREATED_MESSAGE);

    }

    @Override
    @Transactional
    public ApiResponseDto updateLeaveRequest(Long leaveId, LeaveRequestDto leaveRequestDto, String userId, String tenantId) {
        LeaveTransactionEntity userLeave = leaveTransactionRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        // Ensure the leave request is still in pending state
        if (!userLeave.getLeaveStatus().equals(EnumLeaveStatus.PENDING)) {
            throw new RuntimeException("Only pending leave requests can be updated.");
        }


        TenantEntity tenantEntity = tenantRepository.findByIdAndStatus(Long.valueOf(tenantId), EnumStatus.ACTIVE).orElseThrow(
                () -> new RuntimeException("Tenant not found"));

        UserEntity requestUser = userRepository.findByIdAndStatusAndTenantId(
                        Long.valueOf(userId), EnumStatus.ACTIVE, tenantEntity.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        LeaveTransactionEntity leaveTransactionUpdate = LeaveTransactionMapper.leaveUpadteDtoToLeaveTransactionEntity(leaveRequestDto, tenantEntity, requestUser, userLeave);
//        ApproveWorkflowEntity approveWorkflowUpdate=LeaveTransactionMapper.leaveUpadteDtoToApproveWoekflowEntity(leaveRequestDto, tenantEntity, requestUser,userLeave);
        leaveTransactionRepository.save(leaveTransactionUpdate);

        //leaveTransactionRepository.save(leaveTransaction);

        return new ApiResponseDto(HttpStatus.OK.value(), ResponseConstant.LEAVE_UPDATED_MESSAGE);
    }

    @Override
    public List<LeaveRequestDto> getUserLeaveTransactions(Long requestedUserId, Long requesterId) {
        // Fetch the requester details
        UserEntity requester = userRepository.findByIdAndStatus(requesterId, EnumStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        // If requester is ADMIN, allow access to any user's leave transactions
        if (requester.getRole().getName() == EnumRoleType.ADMIN) {
            List<LeaveTransactionEntity> transactions = leaveTransactionRepository.findByUserIdAndStatus(requestedUserId, EnumStatus.ACTIVE);
            return transactions.stream()
                    .map(LeaveTransactionMapper::leaveTransactionEntityToDto)
                    .collect(Collectors.toList());
        } else if (requester.getRole().getName() == EnumRoleType.USER) { // If requester is a USER, allow only self-access
            if (!requesterId.equals(requestedUserId)) {
                throw new RuntimeException("Access denied! Users can only view their own leave transactions.");
            }
            List<LeaveTransactionEntity> transactions = leaveTransactionRepository.findByUserIdAndStatus(requesterId, EnumStatus.ACTIVE);
            return transactions.stream()
                    .map(LeaveTransactionMapper::leaveTransactionEntityToDto)
                    .collect(Collectors.toList());
        } else {

            throw new RuntimeException("Unauthorized access.");
        }
    }


}




