package com.example.LeaveManagementSystem.persistence.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumRoleType {
    ADMIN("Admin"),
    USER("User"),
    MANAGER("Manager");

    private final String value;

    EnumRoleType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
