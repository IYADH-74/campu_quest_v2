package com.apex.campu_quest_v2.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apex.campu_quest_v2.Entities.Classe;
import com.apex.campu_quest_v2.Entities.Task;
import com.apex.campu_quest_v2.Entities.User;
import com.apex.campu_quest_v2.Enums.Role;
import com.apex.campu_quest_v2.Enums.TaskType;
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

    // 1. Create a class
    @PostMapping("/classes")
    public ResponseEntity<Classe> createClass(@RequestBody Classe classe) {
        return ResponseEntity.ok(classeRepository.save(classe));
    }

    // 2. Update a class
    @PutMapping("/classes/{id}")
    public ResponseEntity<Classe> updateClass(@PathVariable Long id, @RequestBody Classe updated) {
        Optional<Classe> opt = classeRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Classe classe = opt.get();
        classe.setClassName(updated.getClassName());
        return ResponseEntity.ok(classeRepository.save(classe));
    }

    // 3. Delete a class
    @DeleteMapping("/classes/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Long id) {
        if (!classeRepository.existsById(id)) return ResponseEntity.notFound().build();
        classeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // 4. List all classes
    @GetMapping("/classes")
    public List<Classe> getAllClasses() {
        return classeRepository.findAll();
    }

    // 5. Assign classes to teacher
    @PostMapping("/teachers/{teacherId}/assign-classes")
    public ResponseEntity<User> assignClassesToTeacher(@PathVariable Integer teacherId, @RequestBody List<Long> classIds) {
        Optional<User> opt = userRepository.findById(teacherId);
        if (opt.isEmpty() || opt.get().getRole() != Role.TEACHER) return ResponseEntity.notFound().build();
        User teacher = opt.get();
        List<Classe> classes = classeRepository.findAllById(classIds);
        teacher.setTeacherClasses(classes);
        return ResponseEntity.ok(userRepository.save(teacher));
    }

    // 6. Publish a global (optional) task
    @PostMapping("/tasks/global")
    public ResponseEntity<Task> publishGlobalTask(@RequestBody Task task) {
        task.setTaskType(TaskType.Global_Challenge); // or other global type as needed
        return ResponseEntity.ok(taskRepository.save(task));
    }

    // 7. List all global tasks
    @GetMapping("/tasks/global")
    public List<Task> getAllGlobalTasks() {
        return taskRepository.findAll().stream().filter(t -> t.getTaskType().name().startsWith("Global")).toList();
    }

    // 8. List global tasks published by this staff
    @GetMapping("/tasks/my-global")
    public List<Task> getMyGlobalTasks(@RequestParam Long staffId) {
        return taskRepository.findAll().stream()
                .filter(t -> t.getTaskType().name().startsWith("Global"))
                .filter(t -> t.getAssignedByUserId() != null && t.getAssignedByUserId().equals(staffId))
                .toList();
    }
}
