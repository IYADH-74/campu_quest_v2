package com.apex.campu_quest_v2.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apex.campu_quest_v2.Entities.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}