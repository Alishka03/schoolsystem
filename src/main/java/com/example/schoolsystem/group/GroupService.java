package com.example.schoolsystem.group;

import com.example.schoolsystem.student.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class GroupService {
    private final GroupRepository groupRepository;
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;

    }
    public List<Group> findAll(){
        return groupRepository.findAll();
    }
    public Group findById(int id){
        return groupRepository.findById(id).get();
    }
    @Transactional
    public void addStudent(int id , Student student){
        groupRepository.findById(id).ifPresent(
                group-> {
                group.setStudents(Collections.singletonList(student));
                }
        );
    }
    @Transactional
    public void save(Group group){
        groupRepository.save(group);
    }
    @Transactional
    public void delete(int id){
        groupRepository.deleteById(id);
    }
}
