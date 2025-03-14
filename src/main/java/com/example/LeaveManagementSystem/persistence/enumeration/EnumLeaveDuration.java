package com.example.LeaveManagementSystem.persistence.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumLeaveDuration {

    FIRST_HALF("FirstHalf"),
    SECOND_HALF("SecondHalf"),
    FULL_DAY("FullDay")      ;

    private final String value;

    EnumLeaveDuration(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
