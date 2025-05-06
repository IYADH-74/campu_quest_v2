package com.apex.campu_quest_v2.FrontendControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeScreenController {

    @GetMapping("/StudentHomePage")
    public String studentHome() {
        return "StudentHomePage";
    }

    @GetMapping("/TeacherHomePage")
    public String teacherHome() {
        return "TeacherHomePage";
    }

    @GetMapping("/AdminHomePage")
    public String adminHome() {
        return "AdminHomePage";
    }

    @GetMapping("/StaffHomePage")
    public String staffHome() {
        return "StaffHomePage";
    }
}
