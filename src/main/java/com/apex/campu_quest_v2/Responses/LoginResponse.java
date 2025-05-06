package com.apex.campu_quest_v2.Responses;

import com.apex.campu_quest_v2.Enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private long expiresIn;
    private Role role; 

    public LoginResponse(String token, long expiresIn, Role role) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.role = role;
    }
}
