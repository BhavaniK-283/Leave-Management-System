package com.example.LeaveManagementSystem.persistence.repository;

import com.example.LeaveManagementSystem.persistence.entity.TenantEntity;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<TenantEntity,Long> {
    Optional<TenantEntity> findByIdAndStatus(Long aLong, EnumStatus active);
}
