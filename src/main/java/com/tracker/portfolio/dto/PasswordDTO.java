package com.tracker.portfolio.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PasswordDTO {

    @NotBlank(message = "Please provide an old password")
    private String oldPassword;

    @NotBlank(message = "Please provide a new password")
    @Size(min = 6, max = 30, message = "Password must contain between 6-30 characters")
    private String newPassword;
}
