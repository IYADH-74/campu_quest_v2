package com.apex.campu_quest_v2.Enums;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.apex.campu_quest_v2.Enums.Permission.ADMIN_CREATE;
import static com.apex.campu_quest_v2.Enums.Permission.ADMIN_DELETE;
import static com.apex.campu_quest_v2.Enums.Permission.ADMIN_READ;
import static com.apex.campu_quest_v2.Enums.Permission.ADMIN_UPDATE;
import static com.apex.campu_quest_v2.Enums.Permission.CLASSE_CREATE;
import static com.apex.campu_quest_v2.Enums.Permission.CLASSE_DELETE;
import static com.apex.campu_quest_v2.Enums.Permission.CLASSE_READ;
import static com.apex.campu_quest_v2.Enums.Permission.CLASSE_UPDATE;
import static com.apex.campu_quest_v2.Enums.Permission.GLOBAL_TASK_ACCEPT;
import static com.apex.campu_quest_v2.Enums.Permission.GLOBAL_TASK_PUBLISH;
import static com.apex.campu_quest_v2.Enums.Permission.GLOBAL_TASK_SUBMIT;
import static com.apex.campu_quest_v2.Enums.Permission.GLOBAL_TASK_VALIDATE;
import static com.apex.campu_quest_v2.Enums.Permission.GLOBAL_TASK_WITHDRAW;
import static com.apex.campu_quest_v2.Enums.Permission.STAFF_CREATE;
import static com.apex.campu_quest_v2.Enums.Permission.STAFF_DELETE;
import static com.apex.campu_quest_v2.Enums.Permission.STAFF_READ;
import static com.apex.campu_quest_v2.Enums.Permission.STAFF_UPDATE;
import static com.apex.campu_quest_v2.Enums.Permission.STUDENT_CREATE;
import static com.apex.campu_quest_v2.Enums.Permission.STUDENT_DELETE;
import static com.apex.campu_quest_v2.Enums.Permission.STUDENT_READ;
import static com.apex.campu_quest_v2.Enums.Permission.STUDENT_UPDATE;
import static com.apex.campu_quest_v2.Enums.Permission.TASK_CREATE;
import static com.apex.campu_quest_v2.Enums.Permission.TASK_DELETE;
import static com.apex.campu_quest_v2.Enums.Permission.TASK_READ;
import static com.apex.campu_quest_v2.Enums.Permission.TASK_UPDATE;
import static com.apex.campu_quest_v2.Enums.Permission.TEACHER_CREATE;
import static com.apex.campu_quest_v2.Enums.Permission.TEACHER_DELETE;
import static com.apex.campu_quest_v2.Enums.Permission.TEACHER_READ;
import static com.apex.campu_quest_v2.Enums.Permission.TEACHER_UPDATE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
  ADMIN(
      Set.of(
          ADMIN_READ,
          ADMIN_UPDATE,
          ADMIN_DELETE,
          ADMIN_CREATE,
          TEACHER_READ,
          TEACHER_UPDATE,
          TEACHER_DELETE,
          TEACHER_CREATE,
          STAFF_READ,
          STAFF_UPDATE,
          STAFF_DELETE,
          STAFF_CREATE,
          STUDENT_READ,
          STUDENT_UPDATE,
          STUDENT_DELETE,
          STUDENT_CREATE,
          TASK_READ,
          TASK_UPDATE,
          TASK_DELETE,
          TASK_CREATE,
          CLASSE_READ,
          CLASSE_UPDATE,
          CLASSE_DELETE,
          CLASSE_CREATE)),
  STAFF(
      Set.of(TASK_READ,
          TASK_UPDATE,
          TASK_DELETE,
          TASK_CREATE,
          CLASSE_READ,
          CLASSE_UPDATE,
          CLASSE_DELETE,
          CLASSE_CREATE,
          GLOBAL_TASK_PUBLISH,
          GLOBAL_TASK_VALIDATE)),
  TEACHER(Set.of(
      TASK_READ,
      TASK_UPDATE,
      TASK_DELETE,
      TASK_CREATE,
      GLOBAL_TASK_PUBLISH,
      GLOBAL_TASK_VALIDATE
  )),
  STUDENT(Set.of(
      TASK_READ,
      TASK_UPDATE,
      GLOBAL_TASK_ACCEPT,
      GLOBAL_TASK_WITHDRAW,
      GLOBAL_TASK_SUBMIT));

  @Getter
  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
        .stream()
        .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
        .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }

}
