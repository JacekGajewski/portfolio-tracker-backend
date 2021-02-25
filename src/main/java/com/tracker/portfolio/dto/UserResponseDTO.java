package com.tracker.portfolio.dto;

import lombok.Data;

@Data
public class UserResponseDTO {

    private long userId;

    private String username;

    public UserResponseDTO(long userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
