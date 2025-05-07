package com.apex.campu_quest_v2.FrontendControllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeScreenController {
  @GetMapping("/student/home")
  public String studentHome() { return "StudentHomePage"; }

  @GetMapping("/teacher/home")
  public String teacherHome() { return "TeacherHomePage"; }

  @GetMapping("/admin/home")
  public String adminHome() { return "AdminHomePage"; }

  @GetMapping("/staff/home")
  public String staffHome() { return "StaffHomePage"; }
}

