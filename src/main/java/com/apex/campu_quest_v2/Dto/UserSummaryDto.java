package com.apex.campu_quest_v2.Dto;

import com.apex.campu_quest_v2.Enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}
