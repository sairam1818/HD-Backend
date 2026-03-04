package com.tmt.ticketsystem.login.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmt.ticketsystem.Factory.TestDataFactory;
import com.tmt.ticketsystem.enums.LoginMessage;
import com.tmt.ticketsystem.enums.Roles;
import com.tmt.ticketsystem.login.model.dto.LoginDto;
import com.tmt.ticketsystem.login.model.response.LoginResponse;
import com.tmt.ticketsystem.login.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @Autowired
    private ObjectMapper objectMapper;

    // ==================== POST /api/login ====================

    @Test
    void login_ShouldReturn200WithSuccess_WhenCredentialsAreValid() throws Exception {
        LoginDto loginDto = TestDataFactory.buildValidLoginDto();

        LoginResponse response = new LoginResponse();
        response.setMessage(LoginMessage.LOGGED_IN_SUCCESS);
        response.setRole(Roles.USER);
        response.setUserName("test_user");

        when(loginService.login(any(LoginDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("LOGGED_IN_SUCCESS")))
                .andExpect(jsonPath("$.role", is("USER")))
                .andExpect(jsonPath("$.userName", is("test_user")));
    }

    @Test
    void login_ShouldReturn200WithInvalidCredentials_WhenPasswordIsWrong() throws Exception {
        LoginDto loginDto = TestDataFactory.buildInvalidPasswordLoginDto();

        LoginResponse response = new LoginResponse();
        response.setMessage(LoginMessage.INVALID_CREDENTIALS);

        when(loginService.login(any(LoginDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("INVALID_CREDENTIALS")));
    }

    @Test
    void login_ShouldReturn200WithUserNotFound_WhenUserDoesNotExist() throws Exception {
        LoginDto loginDto = TestDataFactory.buildNonExistentUserLoginDto();

        LoginResponse response = new LoginResponse();
        response.setMessage(LoginMessage.USER_NOT_FOUND);

        when(loginService.login(any(LoginDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("USER_NOT_FOUND")));
    }

    @Test
    void login_ShouldReturn200WithAdminRole_WhenAdminLogsIn() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUserName("admin_user");
        loginDto.setPassword("admin123");

        LoginResponse response = new LoginResponse();
        response.setMessage(LoginMessage.LOGGED_IN_SUCCESS);
        response.setRole(Roles.ADMIN);
        response.setUserName("admin_user");

        when(loginService.login(any(LoginDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role", is("ADMIN")))
                .andExpect(jsonPath("$.userName", is("admin_user")));
    }
}
