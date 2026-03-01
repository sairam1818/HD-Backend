package com.tmt.ticketsystem.login;

import com.tmt.ticketsystem.Factory.TestDataFactory;
import com.tmt.ticketsystem.login.converters.UserConverter;
import com.tmt.ticketsystem.login.model.dto.UserDto;
import com.tmt.ticketsystem.login.model.entity.User;
import com.tmt.ticketsystem.login.repository.UserRepository;
import com.tmt.ticketsystem.login.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserConverter userConverter;

    @InjectMocks
    UserService userService;

    @Test
    void addUserShouldAddUserSuccessfully() {

        System.out.println("my first unit testing : ");

        UserDto userDto = new UserDto();
        userDto.setUserName("sairam");

        User userEntity = TestDataFactory.buildTestUserEntity();

        // Mock repository behavior
        Mockito.when(userRepository.findByUserName(Mockito.anyString()))
                .thenReturn(null);

        Mockito.when(userConverter.dtoToEntity(Mockito.any(UserDto.class)))
                .thenReturn(userEntity);

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(userEntity);

        Mockito.when(userConverter.entityToDto(Mockito.any(User.class)))
                .thenReturn(userDto);

        UserDto addedUser = userService.addUsers(userDto);

        // Optional assertion
        assertNotNull(addedUser);
        assertEquals("success", addedUser.getMessage());
    }
}