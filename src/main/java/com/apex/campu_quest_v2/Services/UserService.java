package com.apex.campu_quest_v2.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.apex.campu_quest_v2.Entities.User;
import com.apex.campu_quest_v2.Repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public int getOptionalTaskSlotsForLevel(int level) {
        if (level >= 20) return 10;
        if (level >= 18) return 8;
        if (level >= 16) return 7;
        if (level >= 14) return 6;
        if (level >= 12) return 5;
        if (level >= 10) return 4;
        if (level >= 8) return 3;
        if (level >= 5) return 2;
        return 1;
    }
}
