package com.example.schoolsystem.util;

import com.example.schoolsystem.services.StudentDetailsService;
import com.example.schoolsystem.student.Student;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class StudentValidator implements Validator {
    private final StudentDetailsService studentDetailsService;

    public StudentValidator(StudentDetailsService studentDetailsService) {
        this.studentDetailsService = studentDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Student.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Student student = (Student) target;
        try{
            studentDetailsService.loadUserByUsername(student.getUsername());
        } catch (UsernameNotFoundException ignored) {
            return; // все ок, пользователь не найден
        }
        errors.rejectValue("username", "", "Человек с таким именем пользователя уже существует");
    }
}
