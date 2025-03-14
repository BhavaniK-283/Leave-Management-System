package com.example.LeaveManagementSystem.persistence.repository;

import com.example.LeaveManagementSystem.persistence.entity.LeaveTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveTypeRepository extends JpaRepository<LeaveTypeEntity,Long> {
}
