package com.tmt.ticketsystem.login.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmt.ticketsystem.Factory.TestDataFactory;
import com.tmt.ticketsystem.enums.Roles;
import com.tmt.ticketsystem.enums.UserMessage;
import com.tmt.ticketsystem.exception.GlobalExceptionHandler;
import com.tmt.ticketsystem.exception.UserNotFoundException;
import com.tmt.ticketsystem.login.model.dto.UserDto;
import com.tmt.ticketsystem.login.model.response.UserResponse;
import com.tmt.ticketsystem.login.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // ==================== POST /api/users ====================

    @Test
    void addUsers_ShouldReturnCreatedUser() throws Exception {
        UserDto inputDto = TestDataFactory.buildTestUserDto();
        UserDto resultDto = TestDataFactory.buildTestUserDto();
        resultDto.setMessage("success");

        when(userService.addUsers(any(UserDto.class))).thenReturn(resultDto);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", is("test_user")))
                .andExpect(jsonPath("$.role", is("USER")))
                .andExpect(jsonPath("$.message", is("success")));
    }

    @Test
    void addUsers_ShouldReturnAlreadyExist_WhenDuplicate() throws Exception {
        UserDto inputDto = TestDataFactory.buildTestUserDto();
        UserDto resultDto = TestDataFactory.buildTestUserDto();
        resultDto.setMessage(" is already exist");

        when(userService.addUsers(any(UserDto.class))).thenReturn(resultDto);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(" is already exist")));
    }

    // ==================== GET /api/users ====================

    @Test
    void getUsersList_ShouldReturnListOfUsers() throws Exception {
        List<UserDto> userDtos = TestDataFactory.buildUserDtoList();

        when(userService.getUsersList()).thenReturn(userDtos);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userName", is("test_user")))
                .andExpect(jsonPath("$[1].userName", is("admin_user")));
    }

    @Test
    void getUsersList_ShouldReturnEmptyList_WhenNoUsers() throws Exception {
        when(userService.getUsersList()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ==================== DELETE /api/users/{id} ====================

    @Test
    void deleteUserById_ShouldReturnSuccessResponse() throws Exception {
        UserResponse response = TestDataFactory.buildSuccessUserResponse();

        when(userService.deleteUserById(1)).thenReturn(response);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("user deleted successfully")));
    }

    @Test
    void deleteUserById_ShouldReturn404_WhenUserNotFound() throws Exception {
        when(userService.deleteUserById(999))
                .thenThrow(new UserNotFoundException("user not found with id 999"));

        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("user not found with id 999")));
    }
}
