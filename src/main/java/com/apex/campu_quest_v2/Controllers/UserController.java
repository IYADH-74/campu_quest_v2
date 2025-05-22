package com.apex.campu_quest_v2.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apex.campu_quest_v2.Dto.LeaderboardEntryDto;
import com.apex.campu_quest_v2.Dto.UserSummaryDto;
import com.apex.campu_quest_v2.Entities.User;
import com.apex.campu_quest_v2.Enums.Role;
import com.apex.campu_quest_v2.Repositories.UserRepository;
import com.apex.campu_quest_v2.Services.UserService;

import lombok.RequiredArgsConstructor;


@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:5173/")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    @GetMapping("/me")
    public ResponseEntity<UserSummaryDto> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long id = currentUser.getId() != null ? currentUser.getId().longValue() : null;
        UserSummaryDto dto = new UserSummaryDto(
            id,
            currentUser.getEmail(),
            currentUser.getFirstName(),
            currentUser.getLastName(),
            currentUser.getRole()
        );
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/leaderboard")
    public List<LeaderboardEntryDto> getStudentLeaderboard() {
        return userRepository.findAll().stream()
            .filter(u -> u.getRole() == Role.STUDENT && u.getLevel() != null && u.getLevel() >= 1)
            .sorted((a, b) -> Integer.compare(
                b.getXp() != null ? b.getXp() : 0,
                a.getXp() != null ? a.getXp() : 0
            ))
            .map(u -> new LeaderboardEntryDto(
                ((u.getFirstName() != null ? u.getFirstName() : "") + " " + (u.getLastName() != null ? u.getLastName() : "")).trim(),
                u.getLevel() != null ? u.getLevel() : 0,
                u.getXp() != null ? u.getXp() : 0
            ))
            .collect(Collectors.toList());
    }
}