package com.tracker.portfolio.service;

import com.tracker.portfolio.dto.PasswordDTO;
import com.tracker.portfolio.dto.UserDTO;
import com.tracker.portfolio.dto.UserResponseDTO;
import com.tracker.portfolio.entity.User;
import com.tracker.portfolio.enums.UserRole;
import com.tracker.portfolio.exception.BadPasswordException;
import com.tracker.portfolio.exception.NotUniqueUsernameException;
import com.tracker.portfolio.exception.UserNotFoundException;
import com.tracker.portfolio.mapper.UserMapper;
import com.tracker.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthorityService authorityService;
    private final PortfolioService portfolioService;

    public User getUserEntity(long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException("User: " + userId + " not found"));
    }

    public User getUserEntity(String username) {
        return userRepository.findUserByUsername(username).orElse(null);
    }

    public UserResponseDTO getUser(int userId) {
        User user = getUserEntity(userId);
        return userMapper.userToUserResponseDto(user);
    }

    public UserResponseDTO getUser(String username) {
        User user = userRepository.findUserByUsername(username).orElse(null);
        return userMapper.userToUserResponseDto(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.usersListToUsersResponseDtoList(users);
    }

    public void updateUser(UserDTO userDTO) {
        User user = userMapper.userDtoToUserEntity(userDTO);
        userRepository.save(user);
    }

    public void changePassword(int userId, PasswordDTO passwordDTO) {
        User user = getUserEntity(userId);
        if (!user.getPassword().equals(passwordDTO.getOldPassword())) {
            throw new BadPasswordException("Wrong password");
        }
        user.setPassword(passwordDTO.getNewPassword());
    }

    public void changeUsername(int userId, String newUsername) {
        if (getUserEntity(newUsername) != null) {
            throw new NotUniqueUsernameException("Username already exists");
        }
        User user = getUserEntity(userId);
        user.setUsername(newUsername);
    }

    @Transactional
    public void createUser(UserDTO userDTO) {
        if (getUserEntity(userDTO.getUsername()) != null) {
            throw new NotUniqueUsernameException("Username already exists");
        }
        User user = userMapper.userDtoToUserEntity(userDTO);
        User userSaved = userRepository.save(user);
        authorityService.createAuthority(UserRole.USER, userSaved);
        portfolioService.createEmptyPortfolio(userSaved);
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    //    private User addAuthorityToUser(User user, UserRole userRole){
//
//        Authority authority = authorityService.createAuthority(userRole);
//        Set<Authority> authorities = new HashSet<>();
//        authorities.add(authority);
//        user.setAuthorities(authorities);
//        return user;
//    }
}
