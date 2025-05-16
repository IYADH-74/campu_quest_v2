package com.apex.campu_quest_v2.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apex.campu_quest_v2.Dto.ClasseListDto;
import com.apex.campu_quest_v2.Repositories.ClasseRepository;


@RestController
@RequestMapping("/api/v1/classes")
@CrossOrigin(origins = "http://localhost:5173/")
public class ClasseController {

    @Autowired
    private ClasseRepository classeRepository;

    @GetMapping("/all")
    public List<ClasseListDto> getAllClasses() {
        return classeRepository.findAll().stream()
            .map(classe -> new ClasseListDto(classe.getId(), classe.getClassName()))
            .toList();
    }
}
