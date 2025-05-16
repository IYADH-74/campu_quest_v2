package com.apex.campu_quest_v2.Dto;

import lombok.Data;

@Data
public class UpdateStudentDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private Long classeId;
    private Integer level;
    private Integer xp;
}
