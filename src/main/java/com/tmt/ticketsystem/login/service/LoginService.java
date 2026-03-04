package com.tmt.ticketsystem.login.service;

import com.tmt.ticketsystem.enums.LoginMessage;
import com.tmt.ticketsystem.login.model.dto.LoginDto;
import com.tmt.ticketsystem.login.model.entity.User;
import com.tmt.ticketsystem.login.model.response.LoginResponse;
import com.tmt.ticketsystem.login.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class LoginService {

    private final UserRepository userRepository;

    public LoginResponse login(LoginDto loginDto) {
        LoginResponse apiResponse = new LoginResponse();
        // Assuming LoginDto uses fluent accessors (userName(), password())
        // and UserRepository.findByUserName returns Optional<User>
        User optionalUser = userRepository.findByUserName(loginDto.userName());

        if (optionalUser != null) {
            User user = optionalUser;
            if (user.getUserName().equals(loginDto.userName()) && user.getPassword().equals(loginDto.password())) {

                apiResponse.setMessage(LoginMessage.LOGGED_IN_SUCCESS);
                apiResponse.setRole(user.getRole());
                apiResponse.setUserName(user.getUserName());
            } else {
                apiResponse.setMessage(LoginMessage.INVALID_CREDENTIALS);
            }
        }

        else {
            apiResponse.setMessage(LoginMessage.USER_NOT_FOUND);
        }
        return apiResponse;
    }
}
