package com.apex.campu_quest_v2.Enums;



import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.apex.campu_quest_v2.Enums.Permission.ADMIN_CREATE;
import static com.apex.campu_quest_v2.Enums.Permission.ADMIN_DELETE;
import static com.apex.campu_quest_v2.Enums.Permission.ADMIN_READ;
import static com.apex.campu_quest_v2.Enums.Permission.ADMIN_UPDATE;
import static com.apex.campu_quest_v2.Enums.Permission.STAFF_CREATE;
import static com.apex.campu_quest_v2.Enums.Permission.STAFF_DELETE;
import static com.apex.campu_quest_v2.Enums.Permission.STAFF_READ;
import static com.apex.campu_quest_v2.Enums.Permission.STAFF_UPDATE;
import static com.apex.campu_quest_v2.Enums.Permission.STUDENT_CREATE;
import static com.apex.campu_quest_v2.Enums.Permission.STUDENT_DELETE;
import static com.apex.campu_quest_v2.Enums.Permission.STUDENT_READ;
import static com.apex.campu_quest_v2.Enums.Permission.STUDENT_UPDATE;
import static com.apex.campu_quest_v2.Enums.Permission.TEACHER_CREATE;
import static com.apex.campu_quest_v2.Enums.Permission.TEACHER_DELETE;
import static com.apex.campu_quest_v2.Enums.Permission.TEACHER_READ;
import static com.apex.campu_quest_v2.Enums.Permission.TEACHER_UPDATE;




import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
                  STUDENT_CREATE
          )
  ),
    STAFF(Collections.emptySet()),
    TEACHER(Collections.emptySet()),
    STUDENT(Collections.emptySet());

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
