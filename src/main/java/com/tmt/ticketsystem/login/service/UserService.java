package com.tmt.ticketsystem.login.service;

import com.tmt.ticketsystem.enums.UserMessage;
import com.tmt.ticketsystem.exception.UserNotFoundException;
import com.tmt.ticketsystem.login.converters.UserConverter;
import com.tmt.ticketsystem.login.model.dto.UserDto;
import com.tmt.ticketsystem.login.model.entity.User;
import com.tmt.ticketsystem.login.model.response.UserResponse;
import com.tmt.ticketsystem.login.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public UserDto addUsers(UserDto userDto) {
        User user = new User();
        String userName = userDto.getUserName();
        user = userRepository.findByUserName(userName);
        if (user != null) {
            userDto.setMessage(" is already exist");
            return userDto;
        } else {
            user = userConverter.dtoToEntity(userDto);
            userDto = userConverter.entityToDto(userRepository.save(user));
            userDto.setMessage("success");
            return userDto;
        }
    }

    public List<UserDto> getUsersList() {
        return userConverter.entityToDto(userRepository.findAll());
    }

    public UserResponse deleteUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserResponse userResponse = new UserResponse();
            try {
                userRepository.deleteById(user.get().getId());
                userResponse.setMessage(UserMessage.SUCCESS.getMessage());
            } catch (UserNotFoundException u) {
                throw new UserNotFoundException("error while deleting the user with id " + id);
            }
            return userResponse;
        } else {
            throw new UserNotFoundException("user not found with id " + id);
        }
    }
}
