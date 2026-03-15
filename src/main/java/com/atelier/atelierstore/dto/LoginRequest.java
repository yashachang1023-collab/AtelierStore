package com.atelier.atelierstore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.Email;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email format") // 增加格式校验
    private String email;

    @NotBlank(message = "password is required")
    private String password;
}
