package com.apex.campu_quest_v2.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntryDto {
    private String fullName;
    private Integer level;
    private Integer xp;
}
