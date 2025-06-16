package com.tmt.ticketsystem.login.converters;

import com.tmt.ticketsystem.login.model.dto.UserDto;
import com.tmt.ticketsystem.login.model.entity.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class UserConverter {

    private final ModelMapper modelMapper;

    public UserDto entityToDto(User user) {

        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }


    public List<UserDto> entityToDto(List<User> user) {
        return user.stream().map(x -> entityToDto(x)).collect(Collectors.toList());
    }

    public User dtoToEntity(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        return user;
    }

    public List<User> dtoToEntity(List<UserDto> userDto) {
        return userDto.stream().map(x -> dtoToEntity(x)).collect(Collectors.toList());
    }
}
