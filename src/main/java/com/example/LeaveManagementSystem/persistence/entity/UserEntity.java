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
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="name",nullable = false)
    private String name;

    @Column(name = "email_id", nullable = false, length = 50, unique = true)
    private String emailId;

    @Column(name = "user_code", nullable = false, length = 25, unique = true)
    private String userCode;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false,referencedColumnName ="id")
    private RoleEntity role;  // Each user has a role

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false,referencedColumnName ="id")
    private TenantEntity tenant;  // Each user belongs to a tenant (company)

    @ManyToOne
    @JoinColumn(name = "appraiser_id", nullable = false)
    private UserEntity approver;  // The manager who approves leave

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private UserEntity reviewer;  // The manager who reviews leave

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
