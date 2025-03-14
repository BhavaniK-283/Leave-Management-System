package com.example.LeaveManagementSystem.persistence.entity;

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
@Table(name = "leavetype")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false,referencedColumnName ="id")
    private TenantEntity tenant; // Each leave type belongs to a specific tenant

    @Column(name="name",nullable = false, length = 50)
    private String name;

    @Column(name = "is_approver", nullable = false)
    private boolean isApprover; // Whether approval is required

    @Column(name = "is_reviewer", nullable = false)
    private boolean isReviewer; // Whether review is required

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
