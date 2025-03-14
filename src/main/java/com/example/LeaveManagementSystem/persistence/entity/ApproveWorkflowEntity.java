package com.example.LeaveManagementSystem.persistence.entity;


import com.example.LeaveManagementSystem.persistence.enumeration.EnumLeaveStatus;
import com.example.LeaveManagementSystem.persistence.enumeration.EnumStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "approvalWorkflow")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApproveWorkflowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false,referencedColumnName ="id")
    private TenantEntity tenant; // Tenant to which the workflow belongs

    @ManyToOne
    @JoinColumn(name = "leave_transaction_id", nullable = false,referencedColumnName ="id")
    private LeaveTransactionEntity leaveTransaction; // Leave request being approved

    @ManyToOne
    @JoinColumn(name = "approver_id", nullable = false,referencedColumnName ="id")
    private UserEntity approver; // Approver who is reviewing the leave request

    @Enumerated(EnumType.STRING)
    @Column(name = "workflow_status", nullable = false)
    private EnumLeaveStatus workflowStatus; // PENDING, APPROVED, REJECTED

    @Column(name = "remark", length = 100, nullable = true)
    private String remark; // Optional comment from approver

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
