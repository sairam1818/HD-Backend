package com.tmt.ticketsystem.login.model.response;

import com.tmt.ticketsystem.enums.Roles;
import lombok.Data;

@Data
public class LoginResponse {

    private Object message;
    private Roles role;
    private String userName;
}
