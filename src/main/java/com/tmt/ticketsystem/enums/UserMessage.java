package com.tmt.ticketsystem.enums;

public enum UserMessage {
    SUCCESS("user deleted successfully");



    private final String message;

    UserMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
