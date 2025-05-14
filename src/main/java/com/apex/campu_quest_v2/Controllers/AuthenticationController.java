package com.apex.campu_quest_v2.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apex.campu_quest_v2.Dto.LoginUserDto;
import com.apex.campu_quest_v2.Dto.RegisterStudentDto;
import com.apex.campu_quest_v2.Dto.RegisterUserDto;
import com.apex.campu_quest_v2.Dto.UserVerifyDto;
import com.apex.campu_quest_v2.Entities.User;
import com.apex.campu_quest_v2.Responses.LoginResponse;
import com.apex.campu_quest_v2.Services.AuthenticationService;
import com.apex.campu_quest_v2.Services.JwtService;

@CrossOrigin(origins = "http://localhost:5173/") // ALLOWS REQUESTS FROM ALL ORIGINS - OPEN FOR NOW, LOCKDOWN LATER IF NEEDED!
@RestController
@RequestMapping("/auth") // ALL AUTH ROUTES UNDER THIS BASE PATH
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    // INJECT SERVICES THROUGH CONSTRUCTOR
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    // =========================
    // SIGNUP (STUDENT ONLY)
    // =========================
    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterStudentDto registerStudentDto) {
        User registeredUser = authenticationService.signup(registerStudentDto);
        return ResponseEntity.ok(registeredUser);
    }

    // =========================
    // LOGIN (ALL ROLES)
    // =========================
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User user = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(user);
        String role = user.getRole().name(); // ASSUMING ENUM

        return ResponseEntity.ok(
                new LoginResponse(jwtToken, jwtService.getExpirationTime(), role)
        );
    }

    // =========================
    // VERIFY USER ACCOUNT
    // =========================
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody UserVerifyDto userVerifyDto) {
        try {
            authenticationService.verifyUser(userVerifyDto);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Verification failed: " + e.getMessage());
        }
    }

    // =========================
    // RESEND VERIFICATION CODE
    // =========================
    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Failed to resend code: " + e.getMessage());
        }
    }

    // =========================
    // CREATE USER (ADMIN ONLY)
    // =========================
    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody RegisterUserDto registerUserDto) {
        try {
            User createdUser = authenticationService.createUser(registerUserDto);
            return ResponseEntity.ok(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Failed to create user: " + e.getMessage());
        }
    }
}
