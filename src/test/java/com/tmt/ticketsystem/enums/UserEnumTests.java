package com.tmt.ticketsystem.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserEnumTests {

    // ==================== Roles Enum ====================

    @Test
    void roles_ShouldHaveThreeValues() {
        assertEquals(3, Roles.values().length);
    }

    @Test
    void roles_AdminShouldReturnCorrectRole() {
        assertEquals("admin", Roles.ADMIN.getRole());
    }

    @Test
    void roles_SuperAdminShouldReturnCorrectRole() {
        assertEquals("super_admin", Roles.SUPER_ADMIN.getRole());
    }

    @Test
    void roles_UserShouldReturnCorrectRole() {
        assertEquals("user", Roles.USER.getRole());
    }

    @Test
    void roles_ValueOfShouldWork() {
        assertEquals(Roles.ADMIN, Roles.valueOf("ADMIN"));
        assertEquals(Roles.SUPER_ADMIN, Roles.valueOf("SUPER_ADMIN"));
        assertEquals(Roles.USER, Roles.valueOf("USER"));
    }

    @Test
    void roles_ValueOfShouldThrowForInvalid() {
        assertThrows(IllegalArgumentException.class, () -> Roles.valueOf("UNKNOWN"));
    }

    // ==================== LoginMessage Enum ====================

    @Test
    void loginMessage_ShouldHaveThreeValues() {
        assertEquals(3, LoginMessage.values().length);
    }

    @Test
    void loginMessage_LoggedInSuccessShouldReturnCorrectMessage() {
        assertEquals("logged in successful", LoginMessage.LOGGED_IN_SUCCESS.getMessage());
    }

    @Test
    void loginMessage_InvalidCredentialsShouldReturnCorrectMessage() {
        assertEquals("Invalid credentials", LoginMessage.INVALID_CREDENTIALS.getMessage());
    }

    @Test
    void loginMessage_UserNotFoundShouldReturnCorrectMessage() {
        assertEquals("user not found", LoginMessage.USER_NOT_FOUND.getMessage());
    }

    @Test
    void loginMessage_ValueOfShouldWork() {
        assertEquals(LoginMessage.LOGGED_IN_SUCCESS, LoginMessage.valueOf("LOGGED_IN_SUCCESS"));
        assertEquals(LoginMessage.INVALID_CREDENTIALS, LoginMessage.valueOf("INVALID_CREDENTIALS"));
        assertEquals(LoginMessage.USER_NOT_FOUND, LoginMessage.valueOf("USER_NOT_FOUND"));
    }

    @Test
    void loginMessage_ValueOfShouldThrowForInvalid() {
        assertThrows(IllegalArgumentException.class, () -> LoginMessage.valueOf("NONEXISTENT"));
    }

    // ==================== UserMessage Enum ====================

    @Test
    void userMessage_ShouldHaveOneValue() {
        assertEquals(1, UserMessage.values().length);
    }

    @Test
    void userMessage_SuccessShouldReturnCorrectMessage() {
        assertEquals("user deleted successfully", UserMessage.SUCCESS.getMessage());
    }

    @Test
    void userMessage_ValueOfShouldWork() {
        assertEquals(UserMessage.SUCCESS, UserMessage.valueOf("SUCCESS"));
    }

    @Test
    void userMessage_ValueOfShouldThrowForInvalid() {
        assertThrows(IllegalArgumentException.class, () -> UserMessage.valueOf("FAILURE"));
    }
}
