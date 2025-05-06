package com.apex.campu_quest_v2.FrontendControllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.apex.campu_quest_v2.Dto.RegisterStudentDto;

@Controller
public class SignUpScreenController {

    @GetMapping("/signup")
    public String getSignupPage(Model model) {
        model.addAttribute("user", new RegisterStudentDto());
        return "SignUpPage"; 
    }

    @GetMapping("/verify")
    public String getVerificationPage() {
        return "VerificationPage"; 
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "LoginPage"; 
    }
}
