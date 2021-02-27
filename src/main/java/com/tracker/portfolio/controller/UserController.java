package com.tracker.portfolio.controller;

import com.tracker.portfolio.dto.PasswordDTO;
import com.tracker.portfolio.dto.UserDTO;
import com.tracker.portfolio.dto.UserResponseDTO;
import com.tracker.portfolio.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@CrossOrigin("*")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "/{userId}")
    @ResponseBody
    public UserResponseDTO getUser(@PathVariable int userId) {
        return userService.getUser(userId);
    }

    //    If you want to lock the user for some time after number of tries you should change PUT to POST
    @PutMapping(path = "/{userId}")
    public void updateUser(@Valid @RequestBody UserDTO user) {
        userService.updateUser(user);
    }

    @PatchMapping(path = "/{userId}/password")
    public void changePassword(@PathVariable int userId,
                               @Valid @RequestBody PasswordDTO passwordDTO) {
        userService.changePassword(userId, passwordDTO);
    }

    @PatchMapping(path = "/{userId}/username")
    public void changeUsername(@PathVariable int userId,
                               @RequestParam(value = "new-username") String newUsername) {
        userService.changeUsername(userId, newUsername);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody UserDTO user) {
        userService.createUser(user);
    }

    @DeleteMapping(path = "/{userId}")
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }
}
