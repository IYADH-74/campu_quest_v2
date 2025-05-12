package com.apex.campu_quest_v2.Entities;




import com.apex.campu_quest_v2.Enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "students")
public class Student extends User{

    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;
    @Column(nullable = false)
    private int level;
    @Column(nullable = false)
    private int xp;

    public Student() {
        super();
    }

    public Student(String firstName, String lastName, String username, String email, String password , Classe classe) {
        super(firstName, lastName, username,email, password,Role.ROLE_STUDENT);
        this.classe = classe;
        this.level = 0;
        this.xp = 0;
    }
}
