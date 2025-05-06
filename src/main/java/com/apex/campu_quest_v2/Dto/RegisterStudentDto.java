package com.apex.campu_quest_v2.Dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterStudentDto {
    private String firstName; 
    private String lastName;
    private String username;
    private String email;
    private String password;
    private long classeId;
} 
    

