package com.apex.campu_quest_v2.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apex.campu_quest_v2.Dto.AssignMandatoryTaskDto;
import com.apex.campu_quest_v2.Dto.TaskSubmissionDto;
import com.apex.campu_quest_v2.Entities.StudentTask;
import com.apex.campu_quest_v2.Entities.Task;
import com.apex.campu_quest_v2.Services.TaskService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.apex.campu_quest_v2.Entities.User;
import com.apex.campu_quest_v2.Enums.TaskStatus;
import com.apex.campu_quest_v2.Enums.TaskType;
import com.apex.campu_quest_v2.Repositories.StudentTaskRepository;
import com.apex.campu_quest_v2.Repositories.UserRepository;
import com.apex.campu_quest_v2.Services.UserService;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final StudentTaskRepository studentTaskRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    // Publish a global task (staff/teacher)
    @PostMapping("/global")
    public ResponseEntity<Task> publishGlobalTask(@RequestBody Task task, @RequestParam Long publisherId) {
        return ResponseEntity.ok(taskService.publishGlobalTask(task, publisherId));
    }

    // List all global tasks
    @GetMapping("/global")
    public List<Task> getAllGlobalTasks() {
        return taskService.getAllGlobalTasks();
    }

    @PostMapping("/global/{taskId}/accept")
    public ResponseEntity<StudentTask> acceptGlobalTask(@PathVariable Integer taskId, @RequestParam Integer studentId) {
        return ResponseEntity.ok(taskService.acceptGlobalTask(taskId, studentId));
    }

    // Student withdraws from a global task
    @PostMapping("/global/{taskId}/withdraw")
    public ResponseEntity<?> withdrawGlobalTask(@PathVariable Integer taskId, @RequestParam Integer studentId) {
        taskService.withdrawGlobalTask(taskId, studentId);
        return ResponseEntity.ok().build();
    }

    // Student submits a task
    @PostMapping("/student/{studentTaskId}/submit")
    public ResponseEntity<StudentTask> submitTask(@PathVariable Long studentTaskId, @RequestBody TaskSubmissionDto submissionDto) {
        return ResponseEntity.ok(taskService.submitTask(studentTaskId, submissionDto.getContent()));
    }

    // Staff/teacher validates a submission
    @PostMapping("/student/{studentTaskId}/validate")
    public ResponseEntity<StudentTask> validateTask(@PathVariable Long studentTaskId) {
        return ResponseEntity.ok(taskService.validateTask(studentTaskId));
    }

    // Staff/teacher rejects a submission
    @PostMapping("/student/{studentTaskId}/reject")
    public ResponseEntity<?> rejectTask(@PathVariable Long studentTaskId) {
        taskService.rejectTask(studentTaskId);
        return ResponseEntity.ok().build();
    }

    // Assign a mandatory (class) task (teacher only)
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/mandatory")
    public ResponseEntity<Task> assignMandatoryTask(@RequestBody AssignMandatoryTaskDto dto, @RequestParam Integer teacherId) {
        Task task = taskService.assignMandatoryTask(dto, teacherId);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/optional-task-slots")
    public ResponseEntity<?> getStudentOptionalTaskSlots() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Integer lvlObj = currentUser.getLevel();
        int level = (lvlObj == null) ? 0 : lvlObj;
        int maxSlots = userService.getOptionalTaskSlotsForLevel(level);
        long usedSlots = studentTaskRepository.findByStudentId(currentUser.getId())
            .stream()
            .filter(st -> st.getTask().getTaskType() != null &&
                         (st.getTask().getTaskType() == TaskType.Global_Challenge ||
                          st.getTask().getTaskType() == TaskType.Global_Extra_Course ||
                          st.getTask().getTaskType() == TaskType.Global_Envent_Attendance ||
                          st.getTask().getTaskType() == TaskType.Global_Certification)
            )
            .filter(st -> st.getStatus() == TaskStatus.Pending_Validation)
            .count();
        int availableSlots = maxSlots - (int) usedSlots;
        return ResponseEntity.ok(new java.util.HashMap<String, Integer>() {{
            put("maxSlots", maxSlots);
            put("usedSlots", (int) usedSlots);
            put("availableSlots", availableSlots);
        }});
    }
}
