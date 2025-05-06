package com.apex.campu_quest_v2.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apex.campu_quest_v2.Entities.Classe;
import com.apex.campu_quest_v2.Repositories.ClasseRepository;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "*") 
public class ClasseController {

    @Autowired
    private ClasseRepository classeRepository;

    @GetMapping
    public List<Classe> getAllClasses() {
        return classeRepository.findAll();
    }
}
