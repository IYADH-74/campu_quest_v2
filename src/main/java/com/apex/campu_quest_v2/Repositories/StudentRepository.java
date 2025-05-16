package com.apex.campu_quest_v2.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.apex.campu_quest_v2.Entities.User;

@Repository
public interface  StudentRepository extends CrudRepository<User, Long> {
}
 