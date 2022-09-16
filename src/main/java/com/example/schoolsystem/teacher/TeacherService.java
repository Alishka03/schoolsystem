package com.example.schoolsystem.teacher;

import com.example.schoolsystem.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TeacherService {
    private final TeacherRepository teacherRepository;
    //TODO

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }
    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }
    public Teacher findOne(int id){
        return teacherRepository.findById(id).get();
    }
    @Transactional
    public void save(Teacher teacher){
        teacherRepository.save(teacher);
    }
    @Transactional
    public void delete(int id){
        teacherRepository.deleteById(id);
    }
    @Transactional
    public void update(Teacher tch , int id){
        tch.setId(id);
        teacherRepository.save(tch);
    }
    @Transactional
    public void setTeacher(Teacher tch , Subject subject){
        tch.getSubjects().add(subject);
        subject.setTeacher(tch);
    }
}
