package com.tmt.ticketsystem.Factory;

import com.tmt.ticketsystem.enums.Roles;
import com.tmt.ticketsystem.login.model.dto.LoginDto;
import com.tmt.ticketsystem.login.model.dto.UserDto;
import com.tmt.ticketsystem.login.model.entity.User;
import com.tmt.ticketsystem.login.model.response.LoginResponse;
import com.tmt.ticketsystem.login.model.response.UserResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestDataFactory {

    // ===== User Entity Builders =====

    public static User buildTestUserEntity() {
        User user = new User();
        user.setId(1);
        user.setUserName("test_user");
        user.setPassword("password123");
        user.setRole(Roles.USER);
        return user;
    }

    public static User buildAdminUserEntity() {
        User user = new User();
        user.setId(2);
        user.setUserName("admin_user");
        user.setPassword("admin123");
        user.setRole(Roles.ADMIN);
        return user;
    }

    public static User buildSuperAdminUserEntity() {
        User user = new User();
        user.setId(3);
        user.setUserName("super_admin");
        user.setPassword("super123");
        user.setRole(Roles.SUPER_ADMIN);
        return user;
    }

    public static List<User> buildUserEntityList() {
        return Arrays.asList(buildTestUserEntity(), buildAdminUserEntity(), buildSuperAdminUserEntity());
    }

    // ===== UserDto Builders =====

    public static UserDto buildTestUserDto() {
        UserDto dto = new UserDto();
        dto.setId(1);
        dto.setUserName("test_user");
        dto.setPassword("password123");
        dto.setRole(Roles.USER);
        return dto;
    }

    public static UserDto buildAdminUserDto() {
        UserDto dto = new UserDto();
        dto.setId(2);
        dto.setUserName("admin_user");
        dto.setPassword("admin123");
        dto.setRole(Roles.ADMIN);
        return dto;
    }

    public static List<UserDto> buildUserDtoList() {
        return Arrays.asList(buildTestUserDto(), buildAdminUserDto());
    }

    // ===== LoginDto Builders =====

    public static LoginDto buildValidLoginDto() {
        LoginDto dto = new LoginDto();
        dto.setUserName("test_user");
        dto.setPassword("password123");
        return dto;
    }

    public static LoginDto buildInvalidPasswordLoginDto() {
        LoginDto dto = new LoginDto();
        dto.setUserName("test_user");
        dto.setPassword("wrong_password");
        return dto;
    }

    public static LoginDto buildNonExistentUserLoginDto() {
        LoginDto dto = new LoginDto();
        dto.setUserName("unknown_user");
        dto.setPassword("password123");
        return dto;
    }

    // ===== LoginResponse Builders =====

    public static LoginResponse buildSuccessLoginResponse() {
        LoginResponse response = new LoginResponse();
        response.setUserName("test_user");
        response.setRole(Roles.USER);
        return response;
    }

    // ===== UserResponse Builders =====

    public static UserResponse buildSuccessUserResponse() {
        UserResponse response = new UserResponse();
        response.setMessage("user deleted successfully");
        return response;
    }
}
