package com.example.demo.entity;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String password;
    private String confirmPassword;
}
