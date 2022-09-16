package com.example.schoolsystem.services;

import com.example.schoolsystem.security.StudentDetails;
import com.example.schoolsystem.student.Student;
import com.example.schoolsystem.student.StudentRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class StudentDetailsService implements UserDetailsService {
    private final StudentRepository studentRepository;

    public StudentDetailsService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Student> student = studentRepository.findByUsername(username);
        if(student.isEmpty())
            throw new UsernameNotFoundException("User not found");
        return new StudentDetails(student.get());
    }
}
