package com.tracker.portfolio.mapper;

import com.tracker.portfolio.dto.UserDTO;
import com.tracker.portfolio.dto.UserResponseDTO;
import com.tracker.portfolio.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
                userDTO.getUsername(),
                bCryptPasswordEncoder.encode(userDTO.getPassword())
        );
    }
}
