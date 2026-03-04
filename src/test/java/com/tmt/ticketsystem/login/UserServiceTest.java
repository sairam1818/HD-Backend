package com.tmt.ticketsystem.login;

import com.tmt.ticketsystem.Factory.TestDataFactory;
import com.tmt.ticketsystem.enums.UserMessage;
import com.tmt.ticketsystem.exception.UserNotFoundException;
import com.tmt.ticketsystem.login.converters.UserConverter;
import com.tmt.ticketsystem.login.model.dto.UserDto;
import com.tmt.ticketsystem.login.model.entity.User;
import com.tmt.ticketsystem.login.model.response.UserResponse;
import com.tmt.ticketsystem.login.repository.UserRepository;
import com.tmt.ticketsystem.login.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

        @Mock
        UserRepository userRepository;

        @Mock
        UserConverter userConverter;

        @InjectMocks
        UserService userService;

        // ==================== addUsers ====================

        @Test
        void addUser_ShouldAddUserSuccessfully_WhenUserDoesNotExist() {
                UserDto userDto = TestDataFactory.buildTestUserDto();
                User userEntity = TestDataFactory.buildTestUserEntity();

                when(userRepository.findByUserName(anyString())).thenReturn(null);
                when(userConverter.dtoToEntity(any(UserDto.class))).thenReturn(userEntity);
                when(userRepository.save(any(User.class))).thenReturn(userEntity);
                when(userConverter.entityToDto(any(User.class))).thenReturn(userDto);

                UserResponse result = userService.addUsers(userDto);

                assertNotNull(result);
                assertEquals("success", result.getMessage());
                assertEquals(userDto.userName(), result.getUsers().get(0).userName());
                assertEquals(userDto.password(), result.getUsers().get(0).password());
                verify(userRepository).save(any(User.class));
        }

        @Test
        void addUser_ShouldReturnAlreadyExist_WhenUserAlreadyExists() {
                UserDto userDto = TestDataFactory.buildTestUserDto();
                User existingUser = TestDataFactory.buildTestUserEntity();

                when(userRepository.findByUserName("test_user")).thenReturn(existingUser);

                Exception exception = assertThrows(RuntimeException.class, () -> userService.addUsers(userDto));

                // Verify result
                assertEquals("User " + userDto.userName() + " is already exist", exception.getMessage());
                verify(userRepository, never()).save(any(User.class));
        }

        @Test
        void addUser_ShouldReturnAlreadyExist_WhenAdminUserAlreadyExists() {
                UserDto userDto = TestDataFactory.buildAdminUserDto();
                User existingAdmin = TestDataFactory.buildAdminUserEntity();

                when(userRepository.findByUserName("admin_user")).thenReturn(existingAdmin);

                UserResponse result = userService.addUsers(userDto);

                assertEquals(" is already exist", result.getMessage());
                assertEquals("admin_user", result.getUsers().get(0).userName());
        }

        // ==================== getUsersList ====================

        @Test
        void getUsersList_ShouldReturnListOfUsers() {
                List<User> userEntities = TestDataFactory.buildUserEntityList();
                List<UserDto> userDtos = TestDataFactory.buildUserDtoList();

                when(userRepository.findAll()).thenReturn(userEntities);
                when(userConverter.entityToDto(userEntities)).thenReturn(userDtos);

                List<UserDto> result = userService.getUsersList();

                // Verify result
                assertNotNull(result);
                assertEquals(2, result.size());
                assertEquals(userDtos.get(0).userName(), result.get(0).userName());
                assertEquals(userDtos.get(0).password(), result.get(0).password());
        }

    @Test
    void getUsersList_ShouldReturnEmptyList_WhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(userConverter.entityToDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<UserDto> result = userService.getUsersList();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

        // ==================== deleteUserById ====================

        @Test
        void deleteUserById_ShouldDeleteSuccessfully_WhenUserExists() {
                User user = TestDataFactory.buildTestUserEntity();

                when(userRepository.findById(1)).thenReturn(Optional.of(user));
                doNothing().when(userRepository).deleteById(1);

                UserResponse result = userService.deleteUserById(1);

                assertNotNull(result);
                assertEquals(UserMessage.SUCCESS.getMessage(), result.getMessage());
                verify(userRepository).deleteById(1);
        }

    @Test
    void deleteUserById_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUserById(999)
        );

        assertEquals("user not found with id 999", exception.getMessage());
        verify(userRepository, never()).deleteById(anyInt());
    }

    @Test
    void deleteUserById_ShouldThrowUserNotFoundException_WhenIdIsZero() {
        when(userRepository.findById(0)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUserById(0)
        );
    }

    @Test
    void deleteUserById_ShouldThrowUserNotFoundException_WhenIdIsNegative() {
        when(userRepository.findById(-1)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUserById(-1)
        );

        assertTrue(exception.getMessage().contains("-1"));
    }
}