package com.apex.campu_quest_v2.Entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.apex.campu_quest_v2.Enums.Role;
import com.apex.campu_quest_v2.token.Token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "verification_code")
    private String verificationCode;
    @Column(name = "verification_expiration")
    private LocalDateTime verificationCodeExpiresAt;
    private boolean enabled;

    @OneToMany(mappedBy = "id")
    private List<Token> tokens;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "classe_id")
    private Long classeId;

    @Column
    private Integer level;

    @Column
    private Integer xp;

    @ManyToMany
    @JoinTable(name = "student_tasks", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "task_id"))
    private List<Task> tasks = new java.util.ArrayList<>();

    @Column
    private String material;
    @Column
    private String departement;
    @Column
    private String privilegesDescription;

    @Column(name = "class_ids")
    private String classIdsCsv;

    public User(String firstName, String lastName, String email, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public List<Long> getClassIds() {
        if (classIdsCsv == null || classIdsCsv.isEmpty())
            return List.of();
        String[] parts = classIdsCsv.split(",");
        List<Long> ids = new java.util.ArrayList<>();
        for (String p : parts) {
            try {
                ids.add(Long.parseLong(p.trim()));
            } catch (Exception ignored) {
            }
        }
        return ids;
    }

    public void setClassIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            this.classIdsCsv = null;
        } else {
            this.classIdsCsv = ids.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(","));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
