package com.tmt.ticketsystem.login.model.dto;

import com.tmt.ticketsystem.enums.Roles;

public record UserDto(
        int id,
        String userName,
        Roles role,
        String password,
        String message) {
}
