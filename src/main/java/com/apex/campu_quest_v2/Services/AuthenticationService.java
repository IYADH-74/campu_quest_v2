package com.apex.campu_quest_v2.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apex.campu_quest_v2.Dto.LoginUserDto;
import com.apex.campu_quest_v2.Dto.RegisterStudentDto;
import com.apex.campu_quest_v2.Dto.RegisterUserDto;
import com.apex.campu_quest_v2.Dto.UserVerifyDto;
import com.apex.campu_quest_v2.Entities.Admin;
import com.apex.campu_quest_v2.Entities.Staff;
import com.apex.campu_quest_v2.Entities.Student;
import com.apex.campu_quest_v2.Entities.Teacher;
import com.apex.campu_quest_v2.Entities.User;
import com.apex.campu_quest_v2.Repositories.ClasseRepository;
import com.apex.campu_quest_v2.Repositories.UserRepository;

import jakarta.mail.MessagingException;

@Service
public class AuthenticationService {
    private final ClasseRepository classeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(
            ClasseRepository classeRepository,
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {
        this.classeRepository = classeRepository;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User signup(RegisterStudentDto input) {
        Student user = new Student(
            input.getFirstName(),
            input.getLastName(),
            input.getUsername(),
            input.getEmail(),
            passwordEncoder.encode(input.getPassword()),
            input.getClasseId()
        );
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);
        return userRepository.save(user);
    }

    public User createUser(RegisterUserDto input) {
        logger.debug("Creating user with input: {}", input);
        User user = switch (input.getRole()) {
            case ROLE_TEACHER -> {
                String material = StringUtils.defaultIfBlank(input.getExtraInfo(), "Unknown Material");
                yield new Teacher(
                    input.getFirstName(),
                    input.getLastName(),
                    input.getUsername(),
                    input.getEmail(),
                    passwordEncoder.encode(input.getPassword()),
                    material
                );
            }
            case ROLE_STAFF -> {
                String department = StringUtils.defaultIfBlank(input.getExtraInfo(), "Unknown Department");
                yield new Staff(
                    input.getFirstName(),
                    input.getLastName(),
                    input.getUsername(),
                    input.getEmail(),
                    passwordEncoder.encode(input.getPassword()),
                    department
                );
            }
            case ROLE_ADMIN -> {
                String privileges = StringUtils.defaultIfBlank(input.getExtraInfo(), "Default Privileges");
                yield new Admin(
                    input.getFirstName(),
                    input.getLastName(),
                    input.getUsername(),
                    input.getEmail(),
                    passwordEncoder.encode(input.getPassword()),
                    privileges
                );
            }
            case ROLE_STUDENT -> {
                Student student = new Student(
                    input.getFirstName(),
                    input.getLastName(),
                    input.getUsername(),
                    input.getEmail(),
                    passwordEncoder.encode(input.getPassword()),
                    input.getClasseId()
                );
                student.setTasks(new ArrayList<>()); // Initialize with an empty task list
                yield student;
            }
        };
        user.setEnabled(true); 
        logger.debug("Saved user: {}", userRepository.save(user));
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified. Please verify your account.");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()));

        return user;
    }

    public void verifyUser(UserVerifyDto input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private void sendVerificationEmail(User user) { // TODO: Update with company logo
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app " + user.getFirstName() + "!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue(btw if this is you bel id like to remind you that you are a B.I.T.C.H):</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + user.getVerificationCode() + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), "Account Verification", htmlMessage);
        } catch (MessagingException e) {
            logger.error("Failed to send verification email", e);
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

}
