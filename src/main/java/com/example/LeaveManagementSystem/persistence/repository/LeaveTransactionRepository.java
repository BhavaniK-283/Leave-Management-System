package com.example.LeaveManagementSystem.persistence.repository;

import com.example.LeaveManagementSystem.persistence.entity.LeaveTransactionEntity;
import com.example.LeaveManagementSystem.persistence.entity.UserEntity;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumLeaveStatus;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LeaveTransactionRepository extends JpaRepository<LeaveTransactionEntity,Long> {
    List<LeaveTransactionEntity> findByUserIdAndStatus(Long requestedUserId, EnumStatus enumStatus);

    List<LeaveTransactionEntity> findByUserAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndLeaveStatusIn(UserEntity user, @NotNull(message = "Start Date is required") LocalDate startDate, @NotNull(message = "End Date is required") LocalDate endDate, List<EnumLeaveStatus> pending);

    List<LeaveTransactionEntity> findByUserAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndLeaveStatusIn(UserEntity user, @NotNull(message = "Start Date is required") LocalDate startDate, @NotNull(message = "End Date is required") LocalDate endDate, List<EnumLeaveStatus> pending);
}
