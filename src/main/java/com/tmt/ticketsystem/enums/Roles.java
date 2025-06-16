package com.tmt.ticketsystem.enums;

public enum Roles {
    ADMIN("admin"),
    SUPER_ADMIN("super_admin"),
    USER("user");

    private final String role;

    Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
