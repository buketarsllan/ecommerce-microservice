package com.example.user.dto;

import com.example.user.entity.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private Role role; // optional 
}