package com.apex.campu_quest_v2.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apex.campu_quest_v2.Dto.UserSummaryDto;
import com.apex.campu_quest_v2.Entities.Classe;
import com.apex.campu_quest_v2.Entities.User;
import com.apex.campu_quest_v2.Enums.Role;
import com.apex.campu_quest_v2.Repositories.ClasseRepository;
import com.apex.campu_quest_v2.Repositories.TaskRepository;
import com.apex.campu_quest_v2.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffController {
    private final ClasseRepository classeRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    //Assign classes to teacher
    @PostMapping("/teachers/{teacherId}/assign-classes")
    public ResponseEntity<String> assignClassesToTeacher(@PathVariable Integer teacherId, @RequestBody List<Long> classIds) {
        Optional<User> opt = userRepository.findById(teacherId);
        if (opt.isEmpty() || opt.get().getRole() != Role.TEACHER) {
            return ResponseEntity.badRequest().body("Teacher not found or not a teacher");
        }
        User teacher = opt.get();
        List<Classe> classes = classeRepository.findAllById(classIds);
        teacher.setTeacherClasses(classes);
        userRepository.save(teacher);
        return ResponseEntity.ok("Classes assigned successfully");
    }

    //Get all teachers
    @GetMapping("/teachers")
    public List<UserSummaryDto> getAllTeachers() {
        return userRepository.findAll().stream()
            .filter(u -> u.getRole() == Role.TEACHER)
            .map(u -> {
                UserSummaryDto dto = new UserSummaryDto();
                dto.setId(u.getId() != null ? u.getId().longValue() : null);
                dto.setFirstName(u.getFirstName());
                dto.setLastName(u.getLastName());
                return dto;
            })
            .toList();
    }

    //Get all tasks assigned by a teacher
    @GetMapping("/teachers/{teacherId}/tasks")
    public List<com.apex.campu_quest_v2.Entities.Task> getAllTasksAssignedByTeacher(@PathVariable Long teacherId) {
        return taskRepository.findAll().stream()
            .filter(t -> t.getAssignedByUserId() != null && t.getAssignedByUserId().equals(teacherId))
            .toList();
    }
}
