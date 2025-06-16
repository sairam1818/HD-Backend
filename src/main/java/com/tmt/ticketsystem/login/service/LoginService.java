package com.tmt.ticketsystem.login.service;

import com.tmt.ticketsystem.enums.LoginMessage;
import com.tmt.ticketsystem.login.model.dto.LoginDto;
import com.tmt.ticketsystem.login.model.entity.User;
import com.tmt.ticketsystem.login.model.response.LoginResponse;
import com.tmt.ticketsystem.login.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LoginService {

    private final UserRepository userRepository;


    public LoginResponse login(LoginDto loginDto) {
        LoginResponse apiResponse = new LoginResponse();
        User user = userRepository.findByUserName(loginDto.getUserName());
        if (user != null) {
            if (user.getUserName().equals(loginDto.getUserName()) && user.getPassword().equals(loginDto.getPassword())) {

                apiResponse.setMessage(LoginMessage.LOGGED_IN_SUCCESS);
                apiResponse.setRole(user.getRole());
                apiResponse.setUserName(user.getUserName());
            } else {
                apiResponse.setMessage(LoginMessage.INVALID_CREDENTIALS);
            }
        }
        
        else 
        {
            apiResponse.setMessage(LoginMessage.USER_NOT_FOUND);
        }
        return apiResponse;
    }
}
