package com.example.LeaveManagementSystem.persistence.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumLeaveStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String value;

    EnumLeaveStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}

