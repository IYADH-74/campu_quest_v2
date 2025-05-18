package com.apex.campu_quest_v2.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apex.campu_quest_v2.Entities.StudentTask;
import com.apex.campu_quest_v2.Entities.Task;
import com.apex.campu_quest_v2.Entities.User;

public interface StudentTaskRepository extends JpaRepository<StudentTask, Long> {
    List<StudentTask> findByStudent(User student);
    List<StudentTask> findByTask(Task task);
    List<StudentTask> findByStudentId(Integer studentId);
    List<StudentTask> findByTaskId(Integer taskId);
}
