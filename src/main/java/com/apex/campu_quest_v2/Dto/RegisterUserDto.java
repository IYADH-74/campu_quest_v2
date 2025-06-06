package com.apex.campu_quest_v2.Dto;

import com.apex.campu_quest_v2.Enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role; 
    private String extraInfo; 
    private Long classeId; 

    public Long getClasseId() {
        return classeId;
    }

    public void setClasseId(Long classeId) {
        this.classeId = classeId;
    }
}
