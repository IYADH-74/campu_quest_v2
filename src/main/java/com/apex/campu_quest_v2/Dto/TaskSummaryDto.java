package com.apex.campu_quest_v2.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskSummaryDto {
    private String title;
    private String type;
    private String tier;
    private String assigner;
}
