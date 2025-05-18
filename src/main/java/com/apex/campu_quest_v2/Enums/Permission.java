package com.apex.campu_quest_v2.Enums;




import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    TEACHER_READ("teacher:read"),
    TEACHER_UPDATE("teacher:update"),
    TEACHER_CREATE("teacher:create"),
    TEACHER_DELETE("teacher:delete"),
    STAFF_READ("staff:read"),
    STAFF_UPDATE("staff:update"),
    STAFF_CREATE("staff:create"),
    STAFF_DELETE("staff:delete"),
    STUDENT_READ("student:read"),
    STUDENT_UPDATE("student:update"),
    STUDENT_CREATE("student:create"),
    STUDENT_DELETE("student:delete"),
    TASK_READ("task:read"),
    TASK_UPDATE("task:update"),
    TASK_CREATE("task:create"),
    TASK_DELETE("task:delete"),
    CLASSE_READ("classe:read"),
    CLASSE_UPDATE("classe:update"),
    CLASSE_CREATE("classe:create"),
    CLASSE_DELETE("classe:delete"),
    GLOBAL_TASK_PUBLISH("global_task:publish"),
    GLOBAL_TASK_VALIDATE("global_task:validate"),
    GLOBAL_TASK_ACCEPT("global_task:accept"),
    GLOBAL_TASK_WITHDRAW("global_task:withdraw"),
    GLOBAL_TASK_SUBMIT("global_task:submit"),;

    @Getter
    private final String permission;
}

