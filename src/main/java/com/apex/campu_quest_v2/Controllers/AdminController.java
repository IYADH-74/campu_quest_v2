package com.apex.campu_quest_v2.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apex.campu_quest_v2.Dto.RegisterUserDto;
import com.apex.campu_quest_v2.Entities.User;
import com.apex.campu_quest_v2.Services.AuthenticationService;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')") // Restrict all endpoints to admins
public class AdminController {

    private final AuthenticationService authenticationService;

    public AdminController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // =========================
    // CREATE USER (ADMIN ONLY)
    // =========================
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody RegisterUserDto registerUserDto) {
        try {
            User createdUser = authenticationService.createUser(registerUserDto);
            return ResponseEntity.ok(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Failed to create user: " + e.getMessage());
        }
    }
}
