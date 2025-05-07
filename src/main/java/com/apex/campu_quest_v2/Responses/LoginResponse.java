package com.apex.campu_quest_v2.Responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private long expiresIn;
    private String role; 

    public LoginResponse(String token, long expiresIn, String role) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.role = role;
    }
}
