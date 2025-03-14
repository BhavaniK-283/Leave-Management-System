package com.example.LeaveManagementSystem.persistence.entity;

import com.example.LeaveManagementSystem.persistence.enumeration.EnumRoleType;
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
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name="name",nullable = false, unique = true)
    private EnumRoleType name;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false,referencedColumnName ="id")
    private TenantEntity tenant; // Each role belongs to a tenant (company)

    @Enumerated(EnumType.STRING)
    @Column(name="status",nullable = false)
    private EnumStatus status;

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
