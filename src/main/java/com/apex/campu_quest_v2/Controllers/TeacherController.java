package com.apex.campu_quest_v2.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apex.campu_quest_v2.Entities.Task;
import com.apex.campu_quest_v2.Enums.TaskType;
import com.apex.campu_quest_v2.Repositories.TaskRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TaskRepository taskRepository;

    // Get all mandatory (class) tasks assigned by this teacher
    @GetMapping("/{teacherId}/mandatory-tasks")
    public List<Task> getAllMandatoryTasksAssigned(@PathVariable Long teacherId) {
        return taskRepository.findAll().stream()
                .filter(t -> t.getAssignedByUserId() != null && t.getAssignedByUserId().equals(teacherId))
                .filter(t -> t.getTaskType() == TaskType.Class_Mandetory)
                .toList();
    }

    // Get all optional (global) tasks published by this teacher
    @GetMapping("/{teacherId}/optional-tasks")
    public List<Task> getAllOptionalTasksPublished(@PathVariable Long teacherId) {
        return taskRepository.findAll().stream()
                .filter(t -> t.getAssignedByUserId() != null && t.getAssignedByUserId().equals(teacherId))
                .filter(t -> t.getTaskType() != TaskType.Class_Mandetory)
                .toList();
    }
}
