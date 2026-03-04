package com.tmt.ticketsystem.login.service;

import com.tmt.ticketsystem.Factory.TestDataFactory;
import com.tmt.ticketsystem.enums.LoginMessage;
import com.tmt.ticketsystem.enums.Roles;
import com.tmt.ticketsystem.login.model.dto.LoginDto;
import com.tmt.ticketsystem.login.model.entity.User;
import com.tmt.ticketsystem.login.model.response.LoginResponse;
import com.tmt.ticketsystem.login.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginService loginService;

    // ==================== Positive Cases ====================

    @Test
    void login_ShouldReturnSuccess_WhenCredentialsAreValid() {
        LoginDto loginDto = TestDataFactory.buildValidLoginDto();
        User user = TestDataFactory.buildTestUserEntity();

        when(userRepository.findByUserName("test_user")).thenReturn(user);

        LoginResponse response = loginService.login(loginDto);

        assertNotNull(response);
        assertEquals(LoginMessage.LOGGED_IN_SUCCESS, response.getMessage());
        assertEquals(Roles.USER, response.getRole());
        assertEquals("test_user", response.getUserName());
    }

    @Test
    void login_ShouldReturnSuccessWithAdminRole_WhenAdminLogsIn() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUserName("admin_user");
        loginDto.setPassword("admin123");

        User admin = TestDataFactory.buildAdminUserEntity();

        when(userRepository.findByUserName("admin_user")).thenReturn(admin);

        LoginResponse response = loginService.login(loginDto);

        assertEquals(LoginMessage.LOGGED_IN_SUCCESS, response.getMessage());
        assertEquals(Roles.ADMIN, response.getRole());
        assertEquals("admin_user", response.getUserName());
    }

    @Test
    void login_ShouldReturnSuccessWithSuperAdminRole_WhenSuperAdminLogsIn() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUserName("super_admin");
        loginDto.setPassword("super123");

        User superAdmin = TestDataFactory.buildSuperAdminUserEntity();

        when(userRepository.findByUserName("super_admin")).thenReturn(superAdmin);

        LoginResponse response = loginService.login(loginDto);

        assertEquals(LoginMessage.LOGGED_IN_SUCCESS, response.getMessage());
        assertEquals(Roles.SUPER_ADMIN, response.getRole());
    }

    // ==================== Negative Cases ====================

    @Test
    void login_ShouldReturnInvalidCredentials_WhenPasswordIsWrong() {
        LoginDto loginDto = TestDataFactory.buildInvalidPasswordLoginDto();
        User user = TestDataFactory.buildTestUserEntity();

        when(userRepository.findByUserName("test_user")).thenReturn(user);

        LoginResponse response = loginService.login(loginDto);

        assertNotNull(response);
        assertEquals(LoginMessage.INVALID_CREDENTIALS, response.getMessage());
        assertNull(response.getRole());
        assertNull(response.getUserName());
    }

    @Test
    void login_ShouldReturnUserNotFound_WhenUserDoesNotExist() {
        LoginDto loginDto = TestDataFactory.buildNonExistentUserLoginDto();

        when(userRepository.findByUserName("unknown_user")).thenReturn(null);

        LoginResponse response = loginService.login(loginDto);

        assertNotNull(response);
        assertEquals(LoginMessage.USER_NOT_FOUND, response.getMessage());
        assertNull(response.getRole());
        assertNull(response.getUserName());
    }

    // ==================== Edge Cases ====================

    @Test
    void login_ShouldReturnUserNotFound_WhenUsernameIsEmpty() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUserName("");
        loginDto.setPassword("password123");

        when(userRepository.findByUserName("")).thenReturn(null);

        LoginResponse response = loginService.login(loginDto);

        assertEquals(LoginMessage.USER_NOT_FOUND, response.getMessage());
    }

    @Test
    void login_ShouldReturnInvalidCredentials_WhenPasswordIsEmpty() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUserName("test_user");
        loginDto.setPassword("");

        User user = TestDataFactory.buildTestUserEntity();
        when(userRepository.findByUserName("test_user")).thenReturn(user);

        LoginResponse response = loginService.login(loginDto);

        assertEquals(LoginMessage.INVALID_CREDENTIALS, response.getMessage());
    }
}
