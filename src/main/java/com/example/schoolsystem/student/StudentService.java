package com.example.schoolsystem.student;

import com.example.schoolsystem.group.Group;
import com.example.schoolsystem.group.GroupRepository;
import com.example.schoolsystem.subject.Subject;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;


    public StudentService(StudentRepository studentRepository, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    public List<Student> orderByLastName() {
        return studentRepository.findByOrderByLastname();
    }

    public Student findOne(int id) {
        return studentRepository.findById(id).get();
    }

    @Transactional
    public void updateStudent(Student student, int id) {
        student.setId(id);
        String pass = student.getPassword();
        String encoded = new BCryptPasswordEncoder().encode(pass);
        student.setPassword(encoded);
        student.setGroup(studentRepository.findById(id).get().getGroup());
        student.setUsername(studentRepository.findById(id).get().getUsername());
        student.setRole("ROLE_USER");
        studentRepository.save(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Transactional
    public void delete(int id) {
        studentRepository.deleteById(id);
    }

    @Transactional
    public void assign(int id, Group group) {
        studentRepository.findById(id).get().setGroup(group);
    }

    @Transactional
    public void setFree(int id) {
        studentRepository.findById(id).get().setGroup(null);
    }

    @Transactional
    public void addSubject(int id, Subject subject) {
        studentRepository.findById(id).get().getSubjects().add((subject));
        subject.getEnrolledStudents().add(studentRepository.findById(id).get());
        studentRepository.save(studentRepository.findById(id).get());
    }

    Optional<Student> findByUsername(String username) {
        return studentRepository.findByUsername(username);
    }

    @Transactional
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Transactional
    public void unfollowSubject(int id, Subject subject) {
        studentRepository.findById(id).get().getSubjects().remove(subject);
    }

    @Transactional
    public void enrollingByCurrentUser(Student student , Subject subject){
        student.getSubjects().add(subject);
    }
    @Transactional
    public void leavesubject(Student student,Subject subject){
        student.getSubjects().remove(subject);
        subject.getEnrolledStudents().remove(student);
    }

    public void editStudent(Student st ,Student student){
        st.setEmail(student.getEmail());
        st.setName(student.getName());
        st.setLastname(student.getLastname());
        if(!student.getPassword().equals("")){
            student.setPassword(student.getPassword());
        }
        studentRepository.save(st);
    }
    @Transactional
    public void editing(Student st,int id){
        st.setId(id);
        System.out.println(st.getName()+"ASKALSKLAKSL");
        studentRepository.save(st);
    }
}
