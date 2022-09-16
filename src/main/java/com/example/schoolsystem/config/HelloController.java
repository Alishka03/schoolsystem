package com.example.schoolsystem.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("/hello")
    public String helloPage(){
        return "hellopage";
    }

}
