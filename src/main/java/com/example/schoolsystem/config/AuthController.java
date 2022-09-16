package com.example.schoolsystem.config;

import com.example.schoolsystem.services.RegistrationService;
import com.example.schoolsystem.student.Student;
import com.example.schoolsystem.util.StudentValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final StudentValidator studentValidator;
    private final RegistrationService registrationService;
    public AuthController(StudentValidator studentValidator, RegistrationService registrationService) {
        this.studentValidator = studentValidator;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }
    @GetMapping("/registration")
    public String reqistrationPage(@ModelAttribute("student") Student student){
        return "auth/registration";
    }
    @PostMapping("/registration")
    public  String performRegistration(@ModelAttribute("student")@Valid Student student,
                                       BindingResult bind){
        studentValidator.validate(student , bind);
        if (bind.hasErrors()) {
            return "auth/registration";
        }

        registrationService.register(student);
        return "redirect:/auth/login";
    }
    @GetMapping("/activate")
    public String activateAccount(){
        return "auth/loadingpage";
    }
    @GetMapping("/activate/{code}")
    public String activate(@PathVariable("code") String code , Model model){
        boolean isActive = registrationService.activateUser(code);
        if (isActive) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found!");
        }
        System.out.println("asasas");
        return "auth/verificated";
    }
}
