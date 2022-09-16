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
    private final MailSender mailSender;

    public RegistrationService(StudentRepository studentRepository, PasswordEncoder passwordEncoder,  MailSender mailSender) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Transactional
    public void register(Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        String code  = RandomString.make(64);

        student.setCode(code);
        student.setRole("ROLE_USER");

        String message = String.format("Hello %s!\n"+"Welcome to Students application. Please visit the next link : http://localhost:8080/auth/activate/%s"  ,
                student.getUsername() ,
                student.getCode());
        System.out.println(message);
        mailSender.send(student.getEmail(),"Activation code" , message);
        studentRepository.save(student);
    }
    public boolean activateUser(String code){
        Student student = studentRepository.findByCode(code);
        if(student ==  null){
            return false;
        }
        studentRepository.save(student);
        return true;
    }
}
