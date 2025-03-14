package com.example.LeaveManagementSystem.persistence.repository;

import com.example.LeaveManagementSystem.persistence.entity.UserEntity;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByIdAndStatus(Long id, EnumStatus enumStatus);
}
