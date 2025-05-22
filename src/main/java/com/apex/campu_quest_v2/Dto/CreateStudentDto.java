package com.apex.campu_quest_v2.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateStudentDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password; 
    private Long classeId;
}
