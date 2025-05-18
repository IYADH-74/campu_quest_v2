package com.apex.campu_quest_v2.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apex.campu_quest_v2.Entities.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByAssignedByUserId(Long userId);
    List<Task> findByTaskType(com.apex.campu_quest_v2.Enums.TaskType taskType);
}
