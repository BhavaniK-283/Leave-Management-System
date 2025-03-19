package com.example.LeaveManagementSystem.persistence.service.serviceImpl;

import com.example.LeaveManagementSystem.persistence.constants.ResponseConstant;
import com.example.LeaveManagementSystem.persistence.dto.ApiResponseDto;
import com.example.LeaveManagementSystem.persistence.dto.LeaveRequestDto;
import com.example.LeaveManagementSystem.persistence.dto.LeaveResponseDto;
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

import java.time.LocalDate;
import java.util.List;


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

        leaveTransactionValidation.validateAndParseDate(leaveRequestDto.getStartDate());
        leaveTransactionValidation.validateAndParseDate(leaveRequestDto.getEndDate());

        LocalDate startDate= LocalDate.parse(leaveRequestDto.getStartDate());
        LocalDate endDate = LocalDate.parse(leaveRequestDto.getEndDate());

        UserEntity requestUser = leaveTransactionValidation.validateUser(userId, tenantEntity.getId());

        UserEntity userLeave = leaveTransactionValidation.validateUser(String.valueOf(leaveRequestDto.getUserId()), tenantEntity.getId());

        LeaveTypeEntity leaveTypeEntity = leaveTransactionValidation.validateLeaveType(String.valueOf(leaveRequestDto.getLeaveTypeId()));

        leaveTransactionValidation.validateLeaveDates(startDate, endDate);

        leaveTransactionValidation.checkDuplicateLeaveRequest(userLeave, leaveRequestDto,startDate,endDate);

        LeaveTransactionEntity leaveTransaction = leaveTransactionMapper.leaveRequestDtoToLeaveTransactionEntity(leaveRequestDto, tenantEntity, requestUser, userLeave, leaveTypeEntity,startDate,endDate);
        leaveTransactionRepository.save(leaveTransaction);

        List<ApproveWorkflowEntity> approveWorkflowEntities = leaveTransactionMapper.leaveRequestDtoToApproveWorkflowEntities(leaveTransaction, tenantEntity, requestUser, userLeave);
        approveWorkflowRepository.saveAll(approveWorkflowEntities);

        //send mail
        emailService.sendLeaveAppliedEmail(userLeave,String.valueOf(leaveRequestDto.getStartDate()), String.valueOf(leaveRequestDto.getEndDate()));

        return new ApiResponseDto(HttpStatus.OK.value(), ResponseConstant.LEAVE_CREATED_MESSAGE);

    }

    @Override

    public ApiResponseDto updateLeaveRequest(Long leaveId, LeaveRequestDto leaveRequestDto, String userId, String tenantId) {
        LeaveTransactionEntity userLeave = leaveTransactionRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        // Ensure the leave request is still in pending state
        if (!userLeave.getLeaveStatus().equals(EnumLeaveStatus.PENDING)) {
            throw new RuntimeException("Only pending leave requests can be updated.");
        }


        TenantEntity tenantEntity = leaveTransactionValidation.validateTenant(tenantId);

        UserEntity requestUser = userRepository.findByIdAndStatusAndTenantId(
                        Long.valueOf(userId), EnumStatus.ACTIVE, tenantEntity.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate startDate= LocalDate.parse(leaveRequestDto.getStartDate());
        LocalDate endDate = LocalDate.parse(leaveRequestDto.getEndDate());


        LeaveTransactionEntity leaveTransactionUpdate = leaveTransactionMapper.leaveUpadteDtoToLeaveTransactionEntity(leaveRequestDto, requestUser, userLeave,startDate,endDate);
//        ApproveWorkflowEntity approveWorkflowUpdate=LeaveTransactionMapper.leaveUpadteDtoToApproveWoekflowEntity(leaveRequestDto, tenantEntity, requestUser,userLeave);
        leaveTransactionRepository.save(leaveTransactionUpdate);

        //leaveTransactionRepository.save(leaveTransaction);

        return new ApiResponseDto(HttpStatus.OK.value(), ResponseConstant.LEAVE_UPDATED_MESSAGE);
    }

    @Override

    public List<LeaveResponseDto> getUserLeaveTransactions(Long userId, Long approverId) {
        List<LeaveTransactionEntity> transactions;

        if (userId != null) {
            // Fetch the user details
            UserEntity user = userRepository.findByIdAndStatus(userId, EnumStatus.ACTIVE)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            // Fetch leave requests that the user applied for
            transactions = leaveTransactionRepository.findByUserIdAndStatus(userId, EnumStatus.ACTIVE);

        } else if (approverId != null) {
            // Fetch the approver details
            UserEntity approver = userRepository.findByIdAndStatus(approverId, EnumStatus.ACTIVE)
                    .orElseThrow(() -> new UserNotFoundException("Approver not found"));

            EnumRoleType role = approver.getRole().getName();

            if (role == EnumRoleType.ADMIN) {
                // Admin can see all leave requests under their tenant
                transactions = leaveTransactionRepository.findByTenantIdAndStatus(approver.getTenant().getId(), EnumStatus.ACTIVE);
            } else if (role == EnumRoleType.MANAGER) {
                // Manager can see leave requests under their team
                transactions = leaveTransactionRepository.findByApproverIdAndStatus(approverId, EnumStatus.ACTIVE);
            } else {
                throw new RuntimeException("Unauthorized access. Only Admins and Managers can view approvals.");
            }

        } else {
            throw new IllegalArgumentException("Either userId or approverId must be provided in the header.");
        }

        return transactions.stream()
                .map(leaveTransactionMapper::leaveTransactionEntityToResponseDto)
                .toList();
    }


}




