package com.apex.campu_quest_v2.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVerifyDto {
    String email;
    String verificationCode;
}
