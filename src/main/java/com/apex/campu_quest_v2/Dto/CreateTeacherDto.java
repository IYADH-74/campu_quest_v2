package com.apex.campu_quest_v2.Dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTeacherDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String material;
    private List<Long> classIds;
}
