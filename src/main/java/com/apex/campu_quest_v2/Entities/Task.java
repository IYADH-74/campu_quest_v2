package com.apex.campu_quest_v2.Entities;

import java.time.LocalDate;

import com.apex.campu_quest_v2.Enums.TaskType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {


    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000) 
    private String description;

    @Column(nullable = false)
    private int tier;

    @Column(nullable = false)
    private boolean mandatory;

    @Column(nullable = false)
    private int baseXP;

    private int bonusXP;

    private LocalDate deadline;

    @Column(nullable = false)
    private Long assignedByUserId;



    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType;


    @ManyToMany
    @JoinTable(
        name = "task_classes",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private java.util.List<Classe> classes = new java.util.ArrayList<>();



    public Task(String title, String description, int tier, boolean mandatory, boolean assignedToGroup,int baseXP, int bonusXP, LocalDate deadline, TaskType taskType, Long assignedByUserId) {
        this.title = title;
        this.description = description;
        this.tier = tier;
        this.mandatory = mandatory;
        this.baseXP = baseXP;
        this.bonusXP = bonusXP;
        this.deadline = deadline;
        this.taskType = taskType;
        this.assignedByUserId = assignedByUserId;
    }
    
}
