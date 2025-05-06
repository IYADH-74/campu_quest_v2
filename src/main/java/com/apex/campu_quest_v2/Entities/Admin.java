package com.apex.campu_quest_v2.Entities;
import com.apex.campu_quest_v2.Enums.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admins")
public class Admin extends User  {

    private String privilegesDescription;
    
    public Admin() {
        super();
    }

    public Admin(String firstName, String lastName, String username,String email, String password, String privilegesDescription) {
        super(firstName, lastName, username,email, password, Role.Admin);
        this.privilegesDescription = privilegesDescription;
    }
    
}
