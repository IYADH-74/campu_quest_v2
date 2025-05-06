package com.apex.campu_quest_v2.Entities;

import com.apex.campu_quest_v2.Enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "staff")
public class Staff extends User {

    @Column(nullable = false)
    private String departement;

    public Staff() {
        super();
    }

    public Staff(String firstName, String lastName, String username,String email, String password, String departement) {
        super(firstName, lastName,  username,email, password, Role.Staff);
        this.departement = departement;
    }

    
}
