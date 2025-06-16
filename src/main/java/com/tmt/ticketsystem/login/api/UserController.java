package com.tmt.ticketsystem.login.api;

import com.tmt.ticketsystem.login.model.dto.UserDto;
import com.tmt.ticketsystem.login.model.response.UserResponse;
import com.tmt.ticketsystem.login.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public UserDto addUsers(@RequestBody UserDto userDto) {
        return userService.addUsers(userDto);
    }
    @GetMapping("/users")
    public List<UserDto> getUsersList() {
        return userService.getUsersList();
    }
    @DeleteMapping("/users/{id}")
    public UserResponse deleteUserById(@PathVariable int id) {
        return userService.deleteUserById(id);
    }
}
