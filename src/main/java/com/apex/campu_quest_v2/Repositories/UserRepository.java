package com.apex.campu_quest_v2.Repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apex.campu_quest_v2.Entities.User;
import com.apex.campu_quest_v2.Enums.Role;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String verificationCode);
    // Find all students in a class
    List<User> findByClasseIdAndRole(Long classeId, Role role);
}