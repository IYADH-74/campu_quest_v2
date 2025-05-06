package com.apex.campu_quest_v2.Entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "classes")
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String className;

    @ManyToMany(mappedBy = "classes")
    private List<Teacher> teachers = new ArrayList<>();

    @OneToMany(mappedBy = "classe")
    private List<Student> students = new ArrayList<>();

    public Classe() {}

    public Classe(String name) {
        this.className = name;
    }


    
}
