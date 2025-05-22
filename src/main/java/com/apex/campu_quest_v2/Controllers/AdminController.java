package com.apex.campu_quest_v2.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apex.campu_quest_v2.Dto.CreateAdminDto;
import com.apex.campu_quest_v2.Dto.CreateStaffDto;
import com.apex.campu_quest_v2.Dto.CreateStudentDto;
import com.apex.campu_quest_v2.Dto.CreateTeacherDto;
import com.apex.campu_quest_v2.Dto.TaskSummaryDto;
import com.apex.campu_quest_v2.Dto.UpdateAdminDto;
import com.apex.campu_quest_v2.Dto.UpdateStaffDto;
import com.apex.campu_quest_v2.Dto.UpdateStudentDto;
import com.apex.campu_quest_v2.Dto.UpdateTeacherDto;
import com.apex.campu_quest_v2.Dto.UserSummaryDto;
import com.apex.campu_quest_v2.Entities.Classe;
import com.apex.campu_quest_v2.Entities.User;
import com.apex.campu_quest_v2.Enums.Role;
import com.apex.campu_quest_v2.Repositories.ClasseRepository;
import com.apex.campu_quest_v2.Repositories.TaskRepository;
import com.apex.campu_quest_v2.Repositories.UserRepository;
import com.apex.campu_quest_v2.Services.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "http://localhost:5173/")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClasseRepository classeRepository;
    private final TaskService taskService;
    private final TaskRepository taskRepository;

    // Create Teacher
    @PostMapping("/teachers")
    public ResponseEntity<User> createTeacher(@RequestBody CreateTeacherDto dto) {
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.TEACHER)
                .material(dto.getMaterial())
                .enabled(true)
                .build();
        if (dto.getClassIds() != null && !dto.getClassIds().isEmpty()) {
            List<Classe> classes = classeRepository.findAllById(dto.getClassIds());
            user.setTeacherClasses(classes);
        }
        return ResponseEntity.ok(userRepository.save(user));
    }

    // Create Staff
    @PostMapping("/staff")
    public ResponseEntity<User> createStaff(@RequestBody CreateStaffDto dto) {
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.STAFF)
                .departement(dto.getDepartement())
                .enabled(true)
                .build();
        return ResponseEntity.ok(userRepository.save(user));
    }

    // Create Student
    @PostMapping("/students")
    public ResponseEntity<User> createStudent(@RequestBody CreateStudentDto dto) {
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.STUDENT)
                .classeId(dto.getClasseId())
                .level(0)
                .xp(0)
                .enabled(true)
                .build();
        return ResponseEntity.ok(userRepository.save(user));
    }

    // Create Admin
    @PostMapping("/admins")
    public ResponseEntity<User> createAdmin(@RequestBody CreateAdminDto dto) {
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.ADMIN)
                .privilegesDescription(dto.getPrivilegesDescription())
                .enabled(true)
                .build();
        return ResponseEntity.ok(userRepository.save(user));
    }

    // Update Teacher
    @PutMapping("/teachers/{id}")
    public ResponseEntity<User> updateTeacher(@PathVariable Integer id, @RequestBody UpdateTeacherDto dto) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty() || userOpt.get().getRole() != Role.TEACHER) return ResponseEntity.notFound().build();
        User user = userOpt.get();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setMaterial(dto.getMaterial());
        if (dto.getClassIds() != null && !dto.getClassIds().isEmpty()) {
            List<Classe> classes = classeRepository.findAllById(dto.getClassIds());
            user.setTeacherClasses(classes);
        } else {
            user.setTeacherClasses(new java.util.ArrayList<>());
        }
        return ResponseEntity.ok(userRepository.save(user));
    }

    // Update Staff
    @PutMapping("/staff/{id}")
    public ResponseEntity<User> updateStaff(@PathVariable Integer id, @RequestBody UpdateStaffDto dto) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty() || userOpt.get().getRole() != Role.STAFF) return ResponseEntity.notFound().build();
        User user = userOpt.get();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setDepartement(dto.getDepartement());
        return ResponseEntity.ok(userRepository.save(user));
    }

    // Update Student
    @PutMapping("/students/{id}")
    public ResponseEntity<User> updateStudent(@PathVariable Integer id, @RequestBody UpdateStudentDto dto) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty() || userOpt.get().getRole() != Role.STUDENT) return ResponseEntity.notFound().build();
        User user = userOpt.get();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setClasseId(dto.getClasseId());
        user.setLevel(dto.getLevel());
        user.setXp(dto.getXp());
        return ResponseEntity.ok(userRepository.save(user));
    }

    // Update Admin
    @PutMapping("/admins/{id}")
    public ResponseEntity<User> updateAdmin(@PathVariable Integer id, @RequestBody UpdateAdminDto dto) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty() || userOpt.get().getRole() != Role.ADMIN) return ResponseEntity.notFound().build();
        User user = userOpt.get();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPrivilegesDescription(dto.getPrivilegesDescription());
        return ResponseEntity.ok(userRepository.save(user));
    }

    // Delete User (any type)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        if (!userRepository.existsById(id)) return ResponseEntity.notFound().build();
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // List all users by type ps: wont be needed sine ce have an all users endpoint
    @GetMapping("/teachers")
    public List<User> getAllTeachers() {
        return userRepository.findAll().stream().filter(u -> u.getRole() == Role.TEACHER).toList();
    }
    @GetMapping("/staff")
    public List<User> getAllStaff() {
        return userRepository.findAll().stream().filter(u -> u.getRole() == Role.STAFF).toList();
    }
    @GetMapping("/students")
    public List<User> getAllStudents() {
        return userRepository.findAll().stream().filter(u -> u.getRole() == Role.STUDENT).toList();
    }
    @GetMapping("/admins")
    public List<User> getAllAdmins() {
        return userRepository.findAll().stream().filter(u -> u.getRole() == Role.ADMIN).toList();
    }

    // Get all users (summary) for admin
    @GetMapping("/users/summary")
    public ResponseEntity<List<UserSummaryDto>> getAllUsersSummary() {
        List<UserSummaryDto> users = userRepository.findAll().stream()
            .map(u -> {
                UserSummaryDto dto = new UserSummaryDto();
                dto.setId(u.getId() != null ? u.getId().longValue() : null);
                dto.setEmail(u.getEmail());
                dto.setFirstName(u.getFirstName());
                dto.setLastName(u.getLastName());
                dto.setRole(u.getRole());
                return dto;
            })
            .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(users);
    }



    // Endpoint to check if global boost is active
    @GetMapping("/global-boost-status")
    public ResponseEntity<Boolean> getGlobalBoostStatus() {
        return ResponseEntity.ok(taskService.isGlobalBoostActive());
    }

    // Endpoint to toggle global boost (activate or deactivate)
    @PostMapping("/toggle-global-boost")
    public ResponseEntity<?> toggleGlobalBoost(@RequestParam boolean enable) {
        if (enable) {
            taskService.applyGlobalBoost();
        } else {
            taskService.removeGlobalBoost();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tasks")
    public List<TaskSummaryDto> getAllTasksForAdmin() {
        return taskRepository.findAll().stream().map(task -> {
            String assigner = null;
            if (task.getAssignedByUserId() != null) {
                var userOpt = userRepository.findById(task.getAssignedByUserId().intValue());
                if (userOpt.isPresent()) {
                    var user = userOpt.get();
                    assigner = user.getFirstName() + " " + user.getLastName();
                }
            }
            return new TaskSummaryDto(
                task.getTitle(),
                task.getTaskType() != null ? task.getTaskType().name() : null,
                String.valueOf(task.getTier()),
                assigner
            );
        }).toList();
    }
}
