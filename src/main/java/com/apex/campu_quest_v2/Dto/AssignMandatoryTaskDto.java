package com.apex.campu_quest_v2.Dto;

import java.util.List;

public class AssignMandatoryTaskDto {
    private String title;
    private String description;
    private int tier;
    private int baseXP;
    private int bonusXP;
    private String deadline; // ISO date string
    private List<Long> classIds;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getTier() { return tier; }
    public void setTier(int tier) { this.tier = tier; }
    public int getBaseXP() { return baseXP; }
    public void setBaseXP(int baseXP) { this.baseXP = baseXP; }
    public int getBonusXP() { return bonusXP; }
    public void setBonusXP(int bonusXP) { this.bonusXP = bonusXP; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public List<Long> getClassIds() { return classIds; }
    public void setClassIds(List<Long> classIds) { this.classIds = classIds; }
}
