package com.apex.campu_quest_v2.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apex.campu_quest_v2.Dto.CreateAdminDto;
import com.apex.campu_quest_v2.Dto.CreateStaffDto;
import com.apex.campu_quest_v2.Dto.CreateStudentDto;
import com.apex.campu_quest_v2.Dto.CreateTeacherDto;
import com.apex.campu_quest_v2.Dto.UpdateAdminDto;
import com.apex.campu_quest_v2.Dto.UpdateStaffDto;
import com.apex.campu_quest_v2.Dto.UpdateStudentDto;
import com.apex.campu_quest_v2.Dto.UpdateTeacherDto;
import com.apex.campu_quest_v2.Entities.Admin;
import com.apex.campu_quest_v2.Entities.Staff;
import com.apex.campu_quest_v2.Entities.Student;
import com.apex.campu_quest_v2.Entities.Teacher;
import com.apex.campu_quest_v2.Repositories.AdminRepository;
import com.apex.campu_quest_v2.Repositories.StaffRepository;
import com.apex.campu_quest_v2.Repositories.StudentRepository;
import com.apex.campu_quest_v2.Repositories.TeacherRepository;
import com.apex.campu_quest_v2.Repositories.UserRepository;

@Service
public class AdminUserManagementService {
    @Autowired private StudentRepository studentRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private AdminRepository adminRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // CREATE
    public Student createStudent(CreateStudentDto dto) {
        Student student = new Student(
            dto.getFirstName(), dto.getLastName(), dto.getUsername(), dto.getEmail(),
            passwordEncoder.encode(dto.getPassword()), dto.getClasseId()
        );
        return studentRepository.save(student);
    }
    public Teacher createTeacher(CreateTeacherDto dto) {
        // Check for duplicate email
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            System.err.println("[ERROR] User with email already exists: " + dto.getEmail());
            throw new IllegalArgumentException("A user with this email already exists: " + dto.getEmail());
        }
        Teacher teacher = new Teacher(
            dto.getFirstName(), dto.getLastName(), dto.getUsername(), dto.getEmail(),
            passwordEncoder.encode(dto.getPassword()), dto.getMaterial()
        );
        teacher.setClassIds(dto.getClassIds());
        System.out.println("[DEBUG] Attempting to save teacher: " + teacher.getEmail());
        try {
            Teacher saved = teacherRepository.save(teacher);
            System.out.println("[DEBUG] Teacher saved with ID: " + saved.getId());
            return saved;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save teacher: " + e.getMessage());
        }
    }
    public Staff createStaff(CreateStaffDto dto) {
        Staff staff = new Staff(
            dto.getFirstName(), dto.getLastName(), dto.getUsername(), dto.getEmail(),
            passwordEncoder.encode(dto.getPassword()), dto.getDepartement()
        );
        return staffRepository.save(staff);
    }
    public Admin createAdmin(CreateAdminDto dto) {
        Admin admin = new Admin(
            dto.getFirstName(), dto.getLastName(), dto.getUsername(), dto.getEmail(),
            passwordEncoder.encode(dto.getPassword()), dto.getPrivilegesDescription()
        );
        return adminRepository.save(admin);
    }

    // UPDATE
    public Student updateStudent(Long id, UpdateStudentDto dto) {
        var entity = studentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        if (!(entity instanceof Student student)) {
            throw new IllegalArgumentException("User with id " + id + " is not a student");
        }
        if (dto.getFirstName() != null) student.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) student.setLastName(dto.getLastName());
        if (dto.getUsername() != null) student.setUsername(dto.getUsername());
        if (dto.getEmail() != null) student.setEmail(dto.getEmail());
        if (dto.getPassword() != null) student.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getClasseId() != null) student.setClasseId(dto.getClasseId());
        if (dto.getLevel() != null) student.setLevel(dto.getLevel());
        if (dto.getXp() != null) student.setXp(dto.getXp());
        return studentRepository.save(student);
    }
    public Teacher updateTeacher(Long id, UpdateTeacherDto dto) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow();
        if (dto.getFirstName() != null) teacher.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) teacher.setLastName(dto.getLastName());
        if (dto.getUsername() != null) teacher.setUsername(dto.getUsername());
        if (dto.getEmail() != null) teacher.setEmail(dto.getEmail());
        if (dto.getPassword() != null) teacher.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getMaterial() != null) teacher.setMaterial(dto.getMaterial());
        if (dto.getClassIds() != null) teacher.setClassIds(dto.getClassIds());
        return teacherRepository.save(teacher);
    }
    public Staff updateStaff(Long id, UpdateStaffDto dto) {
        Staff staff = staffRepository.findById(id).orElseThrow();
        if (dto.getFirstName() != null) staff.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) staff.setLastName(dto.getLastName());
        if (dto.getUsername() != null) staff.setUsername(dto.getUsername());
        if (dto.getEmail() != null) staff.setEmail(dto.getEmail());
        if (dto.getPassword() != null) staff.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getDepartement() != null) staff.setDepartement(dto.getDepartement());
        return staffRepository.save(staff);
    }
    public Admin updateAdmin(Long id, UpdateAdminDto dto) {
        Admin admin = adminRepository.findById(id).orElseThrow();
        if (dto.getFirstName() != null) admin.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) admin.setLastName(dto.getLastName());
        if (dto.getUsername() != null) admin.setUsername(dto.getUsername());
        if (dto.getEmail() != null) admin.setEmail(dto.getEmail());
        if (dto.getPassword() != null) admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getPrivilegesDescription() != null) admin.setPrivilegesDescription(dto.getPrivilegesDescription());
        return adminRepository.save(admin);
    }

    // DELETE
    public void deleteStudent(Long id) { studentRepository.deleteById(id); }
    public void deleteTeacher(Long id) { teacherRepository.deleteById(id); }
    public void deleteStaff(Long id) { staffRepository.deleteById(id); }
    public void deleteAdmin(Long id) { adminRepository.deleteById(id); }
}
