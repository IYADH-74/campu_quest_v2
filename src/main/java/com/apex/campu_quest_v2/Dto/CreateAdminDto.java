package com.apex.campu_quest_v2.Dto;

import lombok.Data;

@Data
public class CreateAdminDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String privilegesDescription;
}
