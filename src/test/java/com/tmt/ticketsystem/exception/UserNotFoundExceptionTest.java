package com.tmt.ticketsystem.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        UserNotFoundException exception = new UserNotFoundException("user not found");

        assertNotNull(exception);
        assertEquals("user not found", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithIdInMessage() {
        UserNotFoundException exception = new UserNotFoundException("user not found with id 42");

        assertEquals("user not found with id 42", exception.getMessage());
    }

    @Test
    void shouldBeRuntimeException() {
        UserNotFoundException exception = new UserNotFoundException("test");

        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void shouldCreateExceptionWithEmptyMessage() {
        UserNotFoundException exception = new UserNotFoundException("");

        assertEquals("", exception.getMessage());
    }

    @Test
    void shouldBeThrowable() {
        assertThrows(UserNotFoundException.class, () -> {
            throw new UserNotFoundException("thrown");
        });
    }
}
