package com.apex.campu_quest_v2.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apex.campu_quest_v2.Entities.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
}
