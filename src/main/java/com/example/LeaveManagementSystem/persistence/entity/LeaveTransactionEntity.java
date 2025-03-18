package com.example.LeaveManagementSystem.persistence.entity;

import com.example.LeaveManagementSystem.persistence.enumeration.EnumLeaveDuration;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumLeaveStatus;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leavetransaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false,referencedColumnName ="id")
    private TenantEntity tenant; // Each leave request belongs to a tenant

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private UserEntity user; // User applying for leave

    @ManyToOne
    @JoinColumn(name = "leave_type_id", nullable = false,referencedColumnName ="id")
    private LeaveTypeEntity leaveType; // Type of leave being applied for

    @Column(name="remarks",columnDefinition = "TEXT", nullable = true)
    private String remarks; // Optional remarks for leave request

    @ManyToOne
    @JoinColumn(name = "approver_id", nullable = false)
    private UserEntity approver; // Manager who approves leave

    @Enumerated(EnumType.STRING)
    @Column(name = "applied_from", nullable = false)
    private EnumLeaveDuration appliedFrom; // FIRST_HALF, SECOND_HALF, FULL_DAY

    @Enumerated(EnumType.STRING)
    @Column(name = "applied_to", nullable = false)
    private EnumLeaveDuration appliedTo; // FIRST_HALF, SECOND_HALF, FULL_DAY


    @Enumerated(EnumType.STRING)
    @Column(name = "approved_for")
    private EnumLeaveDuration approvedFor; // FIRST_HALF, SECOND_HALF, FULL_DAY (Nullable)

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_status", nullable = false)
    private EnumLeaveStatus leaveStatus; // PENDING, APPROVED, CANCELLED

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name="status",nullable = false)
    private EnumStatus status; // ACTIVE, INACTIVE

    @Column(name = "created_by", nullable = false, length = 25)
    private String createdBy;

    @Column(name = "updated_by", nullable = false, length = 25)
    private String updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
