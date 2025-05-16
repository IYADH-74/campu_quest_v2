package com.apex.campu_quest_v2.Entities;

import java.util.ArrayList;
import java.util.List;

import com.apex.campu_quest_v2.Enums.Role;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
    
    @ElementCollection
    @CollectionTable(name = "teacher_class_ids", joinColumns = @JoinColumn(name = "teacher_id"))
    @Column(name = "class_id")
    private List<Long> classIds = new ArrayList<>();

    public Teacher() {
        super();
    }

    public Teacher(String firstName, String lastName, String username, String email, String password, String material) {
        super(firstName, lastName, username,email, password, Role.ROLE_TEACHER);
        this.material = material;
    }





    
}
