package com.apex.campu_quest_v2.Dto;

import java.util.List;

import lombok.Data;

@Data
public class UpdateTeacherDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String material;
    private List<Long> classIds;
}
