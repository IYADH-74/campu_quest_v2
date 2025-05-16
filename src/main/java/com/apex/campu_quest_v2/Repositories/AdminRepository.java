package com.apex.campu_quest_v2.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apex.campu_quest_v2.Entities.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
