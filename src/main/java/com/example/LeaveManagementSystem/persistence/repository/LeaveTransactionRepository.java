package com.example.LeaveManagementSystem.persistence.repository;

import com.example.LeaveManagementSystem.persistence.entity.LeaveTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveTransactionRepository extends JpaRepository<LeaveTransactionEntity,Long> {
}
