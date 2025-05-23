package com.apex.campu_quest_v2.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apex.campu_quest_v2.Dto.ClasseListDto;
import com.apex.campu_quest_v2.Dto.CreateClasseDto;
import com.apex.campu_quest_v2.Dto.UpdateClasseDto;
import com.apex.campu_quest_v2.Entities.Classe;
import com.apex.campu_quest_v2.Repositories.ClasseRepository;


@RestController
@RequestMapping("/api/v1/classes")
@CrossOrigin(origins = "http://localhost:5173/")
public class ClasseController {
    private final ClasseRepository classeRepository;

    public ClasseController(ClasseRepository classeRepository) {
        this.classeRepository = classeRepository;
    }

    // Create a class (ADMIN/STAFF)
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<Classe> createClass(@RequestBody CreateClasseDto dto) {
        Classe classe = new Classe();
        classe.setClassName(dto.getClassName());
        return ResponseEntity.ok(classeRepository.save(classe));
    }

    // Update a class (ADMIN/STAFF)
    @PutMapping("/{id}")
    public ResponseEntity<Classe> updateClass(@PathVariable Long id, @RequestBody UpdateClasseDto dto) {
        Optional<Classe> opt = classeRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Classe classe = opt.get();
        classe.setClassName(dto.getClassName());
        return ResponseEntity.ok(classeRepository.save(classe));
    }

    // Delete a class (ADMIN/STAFF)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Long id) {
        if (!classeRepository.existsById(id)) return ResponseEntity.notFound().build();
        classeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // List all classes (public, DTO)
    @GetMapping("/all")
    public List<ClasseListDto> getAllClassesPublic() {
        return classeRepository.findAll().stream()
            .map(classe -> new ClasseListDto(classe.getId(), classe.getClassName()))
            .toList();
    }
}
