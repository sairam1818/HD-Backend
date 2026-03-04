package com.tmt.ticketsystem.login.converters;

import com.tmt.ticketsystem.Factory.TestDataFactory;
import com.tmt.ticketsystem.enums.Roles;
import com.tmt.ticketsystem.login.model.dto.UserDto;
import com.tmt.ticketsystem.login.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserConverterTest {

    private UserConverter userConverter;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        userConverter = new UserConverter(modelMapper);
    }

    // ==================== entityToDto (single) ====================

    @Test
    void entityToDto_ShouldConvertUserEntityToDto() {
        User user = TestDataFactory.buildTestUserEntity();

        UserDto result = userConverter.entityToDto(user);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test_user", result.getUserName());
        assertEquals("password123", result.getPassword());
        assertEquals(Roles.USER, result.getRole());
    }

    @Test
    void entityToDto_ShouldConvertAdminEntityToDto() {
        User admin = TestDataFactory.buildAdminUserEntity();

        UserDto result = userConverter.entityToDto(admin);

        assertEquals("admin_user", result.getUserName());
        assertEquals(Roles.ADMIN, result.getRole());
    }

    @Test
    void entityToDto_ShouldHandleEntityWithNullFields() {
        User user = new User();
        user.setId(10);
        // userName, password, role left as null

        UserDto result = userConverter.entityToDto(user);

        assertNotNull(result);
        assertEquals(10, result.getId());
        assertNull(result.getUserName());
        assertNull(result.getPassword());
        assertNull(result.getRole());
    }

    // ==================== entityToDto (list) ====================

    @Test
    void entityToDtoList_ShouldConvertListOfEntities() {
        List<User> users = TestDataFactory.buildUserEntityList();

        List<UserDto> result = userConverter.entityToDto(users);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("test_user", result.get(0).getUserName());
        assertEquals("admin_user", result.get(1).getUserName());
        assertEquals("super_admin", result.get(2).getUserName());
    }

    @Test
    void entityToDtoList_ShouldReturnEmptyList_WhenInputIsEmpty() {
        List<UserDto> result = userConverter.entityToDto(Collections.<User>emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void entityToDtoList_ShouldConvertSingleElementList() {
        List<User> users = Collections.singletonList(TestDataFactory.buildTestUserEntity());

        List<UserDto> result = userConverter.entityToDto(users);

        assertEquals(1, result.size());
        assertEquals("test_user", result.get(0).getUserName());
    }

    // ==================== dtoToEntity (single) ====================

    @Test
    void dtoToEntity_ShouldConvertDtoToEntity() {
        UserDto dto = TestDataFactory.buildTestUserDto();

        User result = userConverter.dtoToEntity(dto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test_user", result.getUserName());
        assertEquals("password123", result.getPassword());
        assertEquals(Roles.USER, result.getRole());
    }

    @Test
    void dtoToEntity_ShouldHandleDtoWithNullFields() {
        UserDto dto = new UserDto();
        dto.setId(5);

        User result = userConverter.dtoToEntity(dto);

        assertNotNull(result);
        assertEquals(5, result.getId());
        assertNull(result.getUserName());
        assertNull(result.getPassword());
        assertNull(result.getRole());
    }

    // ==================== dtoToEntity (list) ====================

    @Test
    void dtoToEntityList_ShouldConvertListOfDtos() {
        List<UserDto> dtos = TestDataFactory.buildUserDtoList();

        List<User> result = userConverter.dtoToEntity(dtos);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test_user", result.get(0).getUserName());
        assertEquals("admin_user", result.get(1).getUserName());
    }

    @Test
    void dtoToEntityList_ShouldReturnEmptyList_WhenInputIsEmpty() {
        List<User> result = userConverter.dtoToEntity(Collections.<UserDto>emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== Round-trip conversion ====================

    @Test
    void roundTrip_EntityToDtoAndBack_ShouldPreserveData() {
        User original = TestDataFactory.buildTestUserEntity();

        UserDto dto = userConverter.entityToDto(original);
        User converted = userConverter.dtoToEntity(dto);

        assertEquals(original.getId(), converted.getId());
        assertEquals(original.getUserName(), converted.getUserName());
        assertEquals(original.getPassword(), converted.getPassword());
        assertEquals(original.getRole(), converted.getRole());
    }
}
