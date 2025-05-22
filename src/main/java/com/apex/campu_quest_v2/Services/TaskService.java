package com.apex.campu_quest_v2.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apex.campu_quest_v2.Dto.AssignMandatoryTaskDto;
import com.apex.campu_quest_v2.Entities.Classe;
import com.apex.campu_quest_v2.Entities.StudentTask;
import com.apex.campu_quest_v2.Entities.Task;
import com.apex.campu_quest_v2.Entities.User;
import com.apex.campu_quest_v2.Enums.Role;
import com.apex.campu_quest_v2.Enums.TaskStatus;
import com.apex.campu_quest_v2.Enums.TaskType;
import com.apex.campu_quest_v2.Repositories.ClasseRepository;
import com.apex.campu_quest_v2.Repositories.StudentTaskRepository;
import com.apex.campu_quest_v2.Repositories.TaskRepository;
import com.apex.campu_quest_v2.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final ClasseRepository classeRepository;

    // XP chart for levels
    private static final int[] LEVEL_TOTAL_XP = {0, 5000, 10500, 16500, 23000, 30000, 37500, 45500, 54500, 64500, 75500, 87500, 101000, 116000, 132500, 150500, 170000, 191000, 213500, 237500, 263500};
    private static final int[] TIER_XP = {2000, 3000, 4000, 5000, 6000};

    // Global boost flag for XP (default false)
    private boolean globalBoostActive = false;
    private LocalDateTime globalBoostActivatedAt = null;

    // Publish a global task (staff/teacher)
    public Task publishGlobalTask(Task task, Long publisherId) {
        task.setAssignedByUserId(publisherId);
        return taskRepository.save(task);
    }

    // List all global tasks
    public List<Task> getAllGlobalTasks() {
        return taskRepository.findAll().stream()
                .filter(t -> t.getTaskType().name().startsWith("Global"))
                .toList();
    }

    // Student accepts a global task
    @Transactional
    public StudentTask acceptGlobalTask(Integer taskId, Integer studentId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        User student = userRepository.findById(studentId).orElseThrow();
        // Prevent duplicate acceptance
        if (studentTaskRepository.findByStudentId(studentId).stream().anyMatch(st -> st.getTask().getId().equals(taskId))) {
            throw new IllegalStateException("Task already accepted");
        }
        StudentTask st = StudentTask.builder()
                .student(student)
                .task(task)
                .status(com.apex.campu_quest_v2.Enums.TaskStatus.In_Progress)
                .assignedAt(java.time.LocalDateTime.now())
                .build();
        return studentTaskRepository.save(st);
    }

    // Student withdraws from a global task
    @Transactional
    public void withdrawGlobalTask(Integer taskId, Integer studentId) {
        List<StudentTask> sts = studentTaskRepository.findByStudentId(studentId).stream()
                .filter(st -> st.getTask().getId().equals(taskId))
                .toList();
        for (StudentTask st : sts) {
            studentTaskRepository.delete(st);
        }
    }

    // Student submits a task
    @Transactional
    public StudentTask submitTask(Long studentTaskId, String submissionContent) {
        StudentTask st = studentTaskRepository.findById(studentTaskId).orElseThrow();
        st.setStatus(com.apex.campu_quest_v2.Enums.TaskStatus.Pending_Validation);
        st.setSubmittedAt(java.time.LocalDateTime.now());
        st.setSubmissionContent(submissionContent);
        return studentTaskRepository.save(st);
    }

    // Staff/teacher validates a submission
    @Transactional
    public StudentTask validateTask(Long studentTaskId) {
        StudentTask st = studentTaskRepository.findById(studentTaskId).orElseThrow();
        st.setStatus(com.apex.campu_quest_v2.Enums.TaskStatus.Complete);
        int xp = getXpForTier(st.getTask().getTier());
        if (st.getTask().getTaskType().name().startsWith("Global") && isGlobalBoostActive()) {
            xp = (int) Math.round(xp * 1.2);
        }
        st.setXpAwarded(xp);
        st.setRejected(false);
        // Update student XP/level
        User student = st.getStudent();
        student.setXp((student.getXp() == null ? 0 : student.getXp()) + xp);
        student.setLevel(getLevelForXp(student.getXp()));
        userRepository.save(student);
        return studentTaskRepository.save(st);
    }

    // Staff/teacher rejects a submission (global: remove, mandatory: fail)
    @Transactional
    public void rejectTask(Long studentTaskId) {
        StudentTask st = studentTaskRepository.findById(studentTaskId).orElseThrow();
        TaskType type = st.getTask().getTaskType();
        if (type.name().startsWith("Global")) {
            studentTaskRepository.delete(st); // Remove from student list, no penalty
        } else {
            st.setStatus(com.apex.campu_quest_v2.Enums.TaskStatus.Failed);
            st.setRejected(true);
            st.setXpAwarded(-2000); // Flat penalty
            User student = st.getStudent();
            student.setXp((student.getXp() == null ? 0 : student.getXp()) - 2000);
            student.setLevel(getLevelForXp(student.getXp()));
            userRepository.save(student);
            studentTaskRepository.save(st);
        }
    }

    // Assign a mandatory (class) task to classes and all their students
    @Transactional
    public Task assignMandatoryTask(AssignMandatoryTaskDto dto, Integer teacherId) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setTier(dto.getTier());
        task.setMandatory(true);
        task.setBaseXP(dto.getBaseXP());
        task.setBonusXP(dto.getBonusXP());
        task.setDeadline(LocalDate.parse(dto.getDeadline()));
        task.setAssignedByUserId(Long.valueOf(teacherId));
        task.setTaskType(TaskType.Class_Mandetory);
        List<Classe> classes = new ArrayList<>();
        for (Long classId : dto.getClassIds()) {
            classeRepository.findById(classId).ifPresent(classes::add);
        }
        task.setClasses(classes);
        Task savedTask = taskRepository.save(task);
        for (Classe classe : classes) {
            List<User> students = userRepository.findByClasseIdAndRole(classe.getId(), Role.STUDENT);
            for (User student : students) {
                StudentTask st = StudentTask.builder()
                        .student(student)
                        .task(savedTask)
                        .status(TaskStatus.In_Progress)
                        .assignedAt(java.time.LocalDateTime.now())
                        .build();
                studentTaskRepository.save(st);
            }
        }
        return savedTask;
    }

    // XP for tier
    public int getXpForTier(int tier) {
        if (tier < 1 || tier > 5) return 0;
        return TIER_XP[tier - 1];
    }

    // Level for XP
    public int getLevelForXp(int xp) {
        for (int i = LEVEL_TOTAL_XP.length - 1; i >= 0; i--) {
            if (xp >= LEVEL_TOTAL_XP[i]) return i;
        }
        return 0;
    }

    // Scheduled method to auto-fail overdue mandatory tasks
    @Transactional
    public void autoFailOverdueMandatoryTasks() {
        LocalDateTime now = LocalDateTime.now();
        List<StudentTask> inProgress = studentTaskRepository.findAll().stream()
            .filter(st -> st.getStatus() == TaskStatus.In_Progress)
            .filter(st -> st.getTask().getTaskType() == TaskType.Class_Mandetory)
            .filter(st -> st.getTask().getDeadline() != null && st.getTask().getDeadline().atStartOfDay().isBefore(now))
            .toList();
        for (StudentTask st : inProgress) {
            st.setStatus(TaskStatus.Failed);
            st.setRejected(true);
            st.setXpAwarded(-2000);
            User student = st.getStudent();
            student.setXp((student.getXp() == null ? 0 : student.getXp()) - 2000);
            student.setLevel(getLevelForXp(student.getXp()));
            userRepository.save(student);
            studentTaskRepository.save(st);
        }
    }

    public void applyGlobalBoost() {
        globalBoostActive = true;
        globalBoostActivatedAt = LocalDateTime.now();
    }

    public boolean isGlobalBoostActive() {
        // Auto-deactivate if more than 24h passed
        if (globalBoostActive && globalBoostActivatedAt != null &&
            globalBoostActivatedAt.plusHours(24).isBefore(LocalDateTime.now())) {
            globalBoostActive = false;
            globalBoostActivatedAt = null;
        }
        return globalBoostActive;
    }

    public void removeGlobalBoost() {
        globalBoostActive = false;
        globalBoostActivatedAt = null;
    }

    // Scheduled check every hour to auto-deactivate boost
    @Scheduled(fixedRate = 60 * 60 * 1000) // every hour
    public void checkGlobalBoostExpiry() {
        isGlobalBoostActive(); // triggers auto-deactivation if needed
    }

    // Core logic methods will be added here (assign, accept, submit, validate, etc.)
}
