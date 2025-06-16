package com.tmt.ticketsystem.login.model.dto;

import com.tmt.ticketsystem.enums.Roles;
import lombok.Data;

@Data
public class UserDto {
    private int id;
    private String userName;
    private Roles role;
    private String password;
    private String message;
}
