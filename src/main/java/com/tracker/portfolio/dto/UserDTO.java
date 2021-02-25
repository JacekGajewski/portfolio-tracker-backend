package com.tracker.portfolio.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDTO {

    private int userId;

    @NotBlank(message = "Please provide a username")
    @Size(min = 3, max = 20, message = "Username must contain between 3-20 characters")
    private String username;

    @NotBlank(message = "Please provide a password")
    @Size(min = 6, max = 30, message = "Password must contain between 6-30 characters")
    private String password;

    public UserDTO(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
}
