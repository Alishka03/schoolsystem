package com.example.schoolsystem.services;

import com.example.schoolsystem.student.Student;
import com.example.schoolsystem.student.StudentRepository;
import net.bytebuddy.utility.RandomString;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegistrationService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;


    public RegistrationService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setRole("ROLE_USER");
        studentRepository.save(student);
    }
    public boolean activateUser(String code){
        Student student = studentRepository.findByCode(code);

        if(student ==  null){
            return false;
        }else{
            student.setEnabled(true);
            studentRepository.save(student);
            return true;
        }

    }
}
