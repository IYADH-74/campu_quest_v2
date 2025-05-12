package com.apex.campu_quest_v2.Entities;

import java.util.ArrayList;
import java.util.List;

import com.apex.campu_quest_v2.Enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "teachers")
public class Teacher extends User {


    @Column
    private String material;
    
    
    
    @ManyToMany
    @JoinTable(
        name = "teacher_classes",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private List<Classe> classes = new ArrayList<>();

    public Teacher() {
        super();
    }

    public Teacher(String firstName, String lastName, String username, String email, String password, String material) {
        super(firstName, lastName, username,email, password, Role.ROLE_TEACHER);
        this.material = material;
    }





    
}
