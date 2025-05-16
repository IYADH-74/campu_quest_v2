package com.apex.campu_quest_v2.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apex.campu_quest_v2.Dto.CreateAdminDto;
import com.apex.campu_quest_v2.Dto.CreateStaffDto;
import com.apex.campu_quest_v2.Dto.CreateStudentDto;
import com.apex.campu_quest_v2.Dto.CreateTeacherDto;
import com.apex.campu_quest_v2.Dto.UpdateAdminDto;
import com.apex.campu_quest_v2.Dto.UpdateStaffDto;
import com.apex.campu_quest_v2.Dto.UpdateStudentDto;
import com.apex.campu_quest_v2.Dto.UpdateTeacherDto;
import com.apex.campu_quest_v2.Services.AdminUserManagementService;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final AdminUserManagementService adminUserManagementService;

    public AdminController(AdminUserManagementService adminUserManagementService) {
        this.adminUserManagementService = adminUserManagementService;
    }

    @PostMapping("/create-student")
    public ResponseEntity<?> createStudent(@RequestBody CreateStudentDto dto) {
        return ResponseEntity.ok(adminUserManagementService.createStudent(dto));
    }

    @PostMapping("/create-teacher")
    public ResponseEntity<?> createTeacher(@RequestBody CreateTeacherDto dto) {
        try {
            return ResponseEntity.ok(adminUserManagementService.createTeacher(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/create-staff")
    public ResponseEntity<?> createStaff(@RequestBody CreateStaffDto dto) {
        return ResponseEntity.ok(adminUserManagementService.createStaff(dto));
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody CreateAdminDto dto) {
        return ResponseEntity.ok(adminUserManagementService.createAdmin(dto));
    }

    @PutMapping("/update-student/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody UpdateStudentDto dto) {
        return ResponseEntity.ok(adminUserManagementService.updateStudent(id, dto));
    }

    @PutMapping("/update-teacher/{id}")
    public ResponseEntity<?> updateTeacher(@PathVariable Long id, @RequestBody UpdateTeacherDto dto) {
        return ResponseEntity.ok(adminUserManagementService.updateTeacher(id, dto));
    }

    @PutMapping("/update-staff/{id}")
    public ResponseEntity<?> updateStaff(@PathVariable Long id, @RequestBody UpdateStaffDto dto) {
        return ResponseEntity.ok(adminUserManagementService.updateStaff(id, dto));
    }

    @PutMapping("/update-admin/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @RequestBody UpdateAdminDto dto) {
        return ResponseEntity.ok(adminUserManagementService.updateAdmin(id, dto));
    }

    @DeleteMapping("/delete-student/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        adminUserManagementService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-teacher/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
        adminUserManagementService.deleteTeacher(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-staff/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable Long id) {
        adminUserManagementService.deleteStaff(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-admin/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        adminUserManagementService.deleteAdmin(id);
        return ResponseEntity.ok().build();
    }
}
