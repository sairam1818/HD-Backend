package com.tmt.ticketsystem.enums;

public enum LoginMessage {
    LOGGED_IN_SUCCESS("logged in successful"),
    INVALID_CREDENTIALS("Invalid credentials"),
    USER_NOT_FOUND("user not found");

    private final String message;

    LoginMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
