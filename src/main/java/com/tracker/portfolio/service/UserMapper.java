package com.tracker.portfolio.service;

import com.tracker.portfolio.dto.UserDTO;
import com.tracker.portfolio.dto.UserResponseDTO;
import com.tracker.portfolio.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public List<UserResponseDTO> usersListToUsersResponseDtoList(List<User> users) {
        if (users == null) return null;
        return users
                .stream()
                .map(this::userToUserResponseDto)
                .collect(Collectors.toList());
    }

    public UserResponseDTO userToUserResponseDto(User user) {
        if (user == null) return null;
        return new UserResponseDTO(
                user.getId(),
                user.getUsername()
        );
    }

    public User userDtoToUserEntity(UserDTO userDTO) {
        return new User(
                userDTO.getUserId(),
                userDTO.getUsername(),
                userDTO.getPassword()
        );
    }
}
