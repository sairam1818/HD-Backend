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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    // @Autowired is removed as we are not using Spring's test context for
    // ObjectMapper
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // ==================== POST /api/users ====================

    @Test
    void addUsers_ShouldReturnCreatedUser() throws Exception {
        UserDto userDto = TestDataFactory.buildTestUserDto();
        List<UserDto> userDtoList = Collections.singletonList(userDto);
        UserResponse resultResponse = new UserResponse("success", userDtoList);

        when(userService.addUsers(any(UserDto.class))).thenReturn(resultResponse);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userList[0].userName", is("test_user")))
                .andExpect(jsonPath("$.userList[0].role", is("USER")))
                .andExpect(jsonPath("$.message", is("success")));
    }

    @Test
    void addUsers_ShouldReturnAlreadyExist_WhenDuplicate() throws Exception {
        UserDto userDto = TestDataFactory.buildTestUserDto();
        List<UserDto> emptyList = Collections.emptyList();
        UserResponse resultResponse = new UserResponse("User " + userDto.userName() + " is already exist", emptyList);

        when(userService.addUsers(any(UserDto.class)))
                .thenThrow(new RuntimeException("User " + userDto.userName() + " is already exist"));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isInternalServerError());
    }

    // ==================== GET /api/users ====================

    @Test
    void getUsersList_ShouldReturnListOfUsers() throws Exception {
        List<UserDto> userDtos = TestDataFactory.buildUserDtoList();
        UserResponse resultResponse = new UserResponse("success", userDtos);

        when(userService.getUsersList()).thenReturn(resultResponse);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.userList[1].userName", is("admin_user")));
    }

    @Test
    void getUsersList_ShouldReturnEmptyList_WhenNoUsers() throws Exception {
        UserResponse resultResponse = new UserResponse("success", Collections.emptyList());
        when(userService.getUsersList()).thenReturn(resultResponse);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userList", hasSize(0)));
    }

    // ==================== DELETE /api/users/{id} ====================

    @Test
    void deleteUserById_ShouldReturnSuccessResponse() throws Exception {
        List<UserDto> emptyList = Collections.emptyList();
        UserResponse response = new UserResponse("user deleted successfully", emptyList);

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
