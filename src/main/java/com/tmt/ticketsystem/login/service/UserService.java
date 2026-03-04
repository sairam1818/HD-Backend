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
        // The original line `User user = new User();` was redundant as `user` was
        // immediately reassigned.
        String userName = userDto.userName(); // Changed getUserName() to userName()
        User existingUser = userRepository.findByUserName(userName); // Renamed `user` to `existingUser` for clarity
        if (existingUser != null) {
            // Cannot set message on immutable UserDto.
            // For now, we'll throw an exception or return a specific DTO indicating
            // failure.
            // As per the instruction, we're removing setMessage().
            throw new RuntimeException("User " + userDto.userName() + " is already exist");
        } else {
            User userToSave = userConverter.dtoToEntity(userDto);
            User savedUser = userRepository.save(userToSave);
            UserDto responseDto = userConverter.entityToDto(savedUser);
            // Cannot set message on immutable UserDto.
            // If a success message is needed, the return type should be a mutable response
            // object.
            return responseDto;
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
