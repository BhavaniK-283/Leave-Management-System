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
@Table(name = "tenant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="name",nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "tenant_prefix", nullable = false, length = 10, unique = true)
    private String tenantPrefix;

    @Enumerated(EnumType.STRING)
    @Column(name="status",nullable = false)
    private EnumStatus status; // Enum for ACTIVE, INACTIVE

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
