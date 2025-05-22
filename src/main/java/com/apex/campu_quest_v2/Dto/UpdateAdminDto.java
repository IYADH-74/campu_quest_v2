package com.apex.campu_quest_v2.Dto;

import lombok.Data;

@Data
public class UpdateAdminDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String privilegesDescription;
}
