package com.apex.campu_quest_v2.Dto;

import com.apex.campu_quest_v2.Enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private Role role; // Role can be ROLE_TEACHER, ROLE_STAFF, or ROLE_ADMIN
    private String extraInfo; // For role-specific data like material, department, or privileges
    private Long classeId; // For students to specify their class

    public Long getClasseId() {
        return classeId;
    }

    public void setClasseId(Long classeId) {
        this.classeId = classeId;
    }
}
