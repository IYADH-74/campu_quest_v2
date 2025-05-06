package com.apex.campu_quest_v2.Entities;

import java.time.LocalDate;

import com.apex.campu_quest_v2.Enums.TaskStatus;
import com.apex.campu_quest_v2.Enums.TaskType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {


     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000) 
    private String description;

    @Column(nullable = false)
    private int tier;

    @Column(nullable = false)
    private boolean mandatory;

    @Column(nullable = false)
    private boolean assignedToGroup;

    @Column(nullable = false)
    private int baseXP;

    private int bonusXP;

    private LocalDate deadline;

    @Column(nullable = false)
    private Long assignedByUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.Available;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType;



    public Task() {}

    public Task(String title, String description, int tier, boolean mandatory, boolean assignedToGroup,int baseXP, int bonusXP, LocalDate deadline, TaskStatus status, TaskType taskType, Long assignedByUserId) {
        this.title = title;
        this.description = description;
        this.tier = tier;
        this.mandatory = mandatory;
        this.assignedToGroup = assignedToGroup;
        this.baseXP = baseXP;
        this.bonusXP = bonusXP;
        this.deadline = deadline;
        this.status = status;
        this.taskType = taskType;
        this.assignedByUserId = assignedByUserId;
    }
    
}
