package com.example.schoolsystem.student;

import com.example.schoolsystem.group.Group;
import com.example.schoolsystem.security.StudentDetails;
import com.example.schoolsystem.subject.Subject;
import com.example.schoolsystem.subject.SubjectService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class StudentController {
    private final StudentService studentService;
    private final SubjectService subjectService;

    public StudentController(StudentService studentService, SubjectService subjectService) {
        this.studentService = studentService;
        this.subjectService = subjectService;
    }

    @GetMapping("/myprofile")
    public String myPage(Principal student, Model model) {
        Group gr = loggedStudent().getGroup();
        boolean flag = loggedStudent().getGroup() == null;
        if (!flag) {
            model.addAttribute("group", loggedStudent().getGroup());
        }
        boolean subjects = loggedStudent().getSubjects() == null;
        for (Subject subject : loggedStudent().getSubjects()) {
            System.out.println(subject.getName());
        }
        model.addAttribute("subjects", subjects);
        if (!subjects) model.addAttribute("mySubjects", loggedStudent().getSubjects());
        model.addAttribute("flag", flag);
        model.addAttribute("student", loggedStudent());

        return "students/mypage";
    }

    @GetMapping("/edit")
    public String editingStudent(Model model) {
        model.addAttribute("student", loggedStudent());
        model.addAttribute("studentToEdit",new Student());
        return "students/edit";
    }

    @GetMapping("/enroll")
    public String enroll(Model model) {
        Student student = loggedStudent();
        model.addAttribute("subjects", gettingSubjects());
        model.addAttribute("subject", new Subject());
        model.addAttribute("enrolledsubjects", student.getSubjects());
        return "students/enroll";
    }

    @PostMapping("/enrolltosubject/{id}")
    public String enrollingToSubject(@PathVariable("id") int id, @ModelAttribute("subject") Subject subject) {
        Subject sub = subjectService.findOne(id).get();
        subjectService.enrollCurrentUser(loggedStudent(), sub);
        studentService.enrollingByCurrentUser(loggedStudent(), sub);
        return "redirect:/enroll";
    }

    @PatchMapping("/editUser")
    public String editing(@ModelAttribute("studentToEdit") Student student){
        System.out.println("Student to edit" + student.getPassword()+student.getName()+student.getUsername()+student.getLastname()+student.getYear());
        studentService.editStudent(loggedStudent(),student);
        return "redirect:/myprofile";
    }
    @PostMapping("/leavesubject/{id}")
    public String leavingSubject(@PathVariable("id") int id, @ModelAttribute("subject") Subject subject) {
        System.out.println(subject.getId() + "SUB:ID , {ID}" + id);
        studentService.leavesubject(loggedStudent(), subjectService.findOne(subject.getId()).get());
        return "redirect:/enroll";
    }


    public Student loggedStudent() {
        StudentDetails st = (StudentDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Student s = studentService.findOne(st.getStudent().getId());
        return s;
    }

    public List<Subject> gettingSubjects() {
        List<Subject> result = new ArrayList<Subject>();
        Student student = loggedStudent();
        Set<Subject> subjectsOfStudent = student.getSubjects();
        List<Subject> temp = new ArrayList<Subject>();
        for (Subject subject : subjectsOfStudent) {
            temp.add(subject);
        }
        List<Subject> allSubjects = subjectService.findAll();
        for (Subject all : allSubjects) {
            if (!temp.contains(all)) {
                result.add(all);
            }
        }
        return result;
    }

}