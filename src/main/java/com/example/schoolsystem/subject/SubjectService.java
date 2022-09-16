package com.example.schoolsystem.subject;

import com.example.schoolsystem.student.Student;
import com.example.schoolsystem.student.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    public SubjectService(SubjectRepository subjectRepository, StudentRepository studentRepository) {
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
    }
    public List<Subject> findAll(){
        return subjectRepository.findAll();
    }
    public Optional<Subject> findOne(int id){
        return Optional.ofNullable(subjectRepository.findById(id).orElse(null));
    }
    public List<Subject> findByName(){
        return subjectRepository.findByOrderByName();
    }
    public Subject toEnroll(int id){
        return subjectRepository.findById(id).get();
    }
    @Transactional
    public void deleteStudent(int id , Subject subject){
        subject.getEnrolledStudents().remove(studentRepository.findById(id).get());
    }
    @Transactional
    public void save(Subject sub){
        subjectRepository.save(sub);
    }
    @Transactional
    public void delete(int id){
        subjectRepository.deleteById(id);
    }


    @Transactional
    public void enrollCurrentUser(Student student , Subject subject){
        subject.getEnrolledStudents().add(student);
    }
}
