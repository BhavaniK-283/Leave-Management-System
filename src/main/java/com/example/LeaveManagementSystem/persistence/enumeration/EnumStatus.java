package com.example.LeaveManagementSystem.persistence.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String value;

    EnumStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
