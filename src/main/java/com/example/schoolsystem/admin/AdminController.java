package com.example.schoolsystem.admin;

import com.example.schoolsystem.group.Group;
import com.example.schoolsystem.group.GroupService;
import com.example.schoolsystem.services.RegistrationService;
import com.example.schoolsystem.student.Student;
import com.example.schoolsystem.student.StudentService;
import com.example.schoolsystem.subject.Subject;
import com.example.schoolsystem.subject.SubjectService;
import com.example.schoolsystem.teacher.Teacher;
import com.example.schoolsystem.teacher.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final StudentService studentService;
    private final GroupService groupService;
    private final RegistrationService registrationService;
    private final SubjectService subjectService;
    private final TeacherService teacherService;

    public AdminController(StudentService studentService, GroupService groupService, RegistrationService registrationService, SubjectService subjectService, TeacherService teacherService) {
        this.studentService = studentService;
        this.groupService = groupService;
        this.registrationService = registrationService;
        this.subjectService = subjectService;
        this.teacherService = teacherService;
    }

    @GetMapping("")
    public String adminMainPage() {
        return "admin/home";
    }

    @GetMapping("/students")
    public String getStudents(Model model) {
        model.addAttribute("students", studentService.orderByLastName());
        System.out.println(groupService.findAll());
        return "admin/allstudents";
    }

    @GetMapping("/students/{id}")
    public String homePage(@PathVariable("id") int id, Model model) {
        Student student = studentService.findOne(id);
        model.addAttribute("student", student);
        Group mygroup = student.getGroup();
        if (mygroup != null) {
            model.addAttribute("mygroup", mygroup);
            System.out.println(groupService.findById(student.getGroup().getId()));
        }
        if (mygroup == null) {
            model.addAttribute("groups", groupService.findAll());
        }
        System.out.println(gettingSubjects(id));
        System.out.println(groupService.findAll());
        Set<Subject> subjects = student.getSubjects();
        boolean flag = (subjects.isEmpty());
        model.addAttribute("subjectsToEnroll", gettingSubjects(id));
        model.addAttribute("flag", flag);
        if (!flag) {
            model.addAttribute("subjects", subjects);
        }
        System.out.println(student.getPassword());
        model.addAttribute("group", new Group());
        model.addAttribute("subject", new Subject());
        return "admin/adminshowstudent";
    }

    @GetMapping("/students/{id}/edit")
    public String editPage(@PathVariable("id") int id, Model model) {
        Student student = studentService.findOne(id);
        System.out.println(student.getPassword());
        model.addAttribute("student", student);
        return "admin/editstudent";
    }

    @GetMapping("/students/new")
    public String newStudent(Model model) {
        model.addAttribute("student", new Student());
        return "admin/newstudent";
    }
    @PostMapping("/students/{id}/addsubject")
    public String addsubject(@PathVariable("id") int id, @ModelAttribute("subject") Subject subject) {
        System.out.println(subject.getId() + "+_+_+_+_+_+_+_+_+_+_+_+_+_+");
        int c = subject.getId();
        System.out.println(subjectService.toEnroll(c).getName());
        studentService.addSubject(id, subjectService.toEnroll(c));
        return "redirect:/admin/students" + "/" + id;
    }

    @PostMapping("/students")
    public String acceptStudent(@ModelAttribute("student") @Valid Student student, BindingResult bind) {
        if (bind.hasErrors()) {
            return "redirect:/admin/students/new";
        }
        registrationService.register(student);
        return "redirect:/admin/students";
    }

    @PatchMapping("/{id}")
    public String submitEdit(@ModelAttribute("student") @Valid Student student, BindingResult bind,
                             @PathVariable("id") int id) {
        if (bind.hasErrors()) {
            return "admin/editstudent";
        }
        studentService.updateStudent(student, id);
        return "redirect:/admin/students";
    }

    @PatchMapping("/students/{id}")
    public String toCheck(@RequestParam("name") String name , @RequestParam("lastname") String lastname
                          ,@RequestParam("email") String email , @RequestParam("year") int year,
                             @PathVariable("id") int id) {
        System.out.println(name);
        System.out.println(lastname);
        System.out.println(email);
        System.out.println(year);
        return "redirect:/admin/students";
    }
    @DeleteMapping("/students/{id}")
    public String deleteStudent(@PathVariable("id") int id) {
        studentService.delete(id);
        return "redirect:/admin/students";
    }

    @PostMapping("/deletefromsubject/{id}/{student_id}")
    public String deleteFromSubject(@PathVariable("id") int id, @PathVariable("student_id") int student_id) {
        studentService.unfollowSubject(student_id, subjectService.findOne(id).get());
        subjectService.deleteStudent(student_id, subjectService.findOne(id).get());
        return "redirect:/admin/subjects/" + id;
    }

    @PatchMapping("/students/{id}/assign")
    public String assignGroup(@PathVariable("id") int id, @ModelAttribute("group") Group group) {
        Group mygroup = groupService.findById(group.getId());
        studentService.assign(id, mygroup);
        return "redirect:/admin/students" + "/" + id;
    }

    @PatchMapping("/students/{id}/setfree")
    public String setFree(@PathVariable("id") int id) {
        studentService.setFree(id);
        return "redirect:/admin/students" + "/" + id;
    }

    //Students DONE->
    @GetMapping("/groups")
    public String getGroup(Model model) {
        model.addAttribute("groups", groupService.findAll());
        return "admin/groups";
    }

    @GetMapping("/groups/{id}")
    public String getGroupById(@PathVariable("id") int id, Model model) {
        model.addAttribute("group", groupService.findById(id));
        model.addAttribute("empty", groupService.findById(id).getStudents().isEmpty());
        model.addAttribute("students", groupService.findById(id).getStudents());
        return "admin/showgroup";
    }

    @GetMapping("/groups/new")
    public String newGroup(Model model) {
        model.addAttribute("group", new Group());
        return "admin/newgroup";
    }

    @PostMapping("/groups/new")
    public String acceptGroup(@ModelAttribute("group") Group group, BindingResult bind) {
        if (bind.hasErrors())
            return "redirect:/admin/groups/new";
        groupService.save(group);
        return "redirect:/admin/groups";
    }

    @DeleteMapping("/groups/{id}")
    public String deleteGroup(@PathVariable("id") int id) {
        groupService.delete(id);
        return "redirect:/admin/groups";
    }

    @GetMapping("/subjects")
    public String allSubjects(Model model) {
        model.addAttribute("subjects", subjectService.findByName());
        return "admin/allsubjects";
    }

    @GetMapping("/subjects/{id}")
    public String showSubject(@PathVariable("id") int id, Model model) {
        model.addAttribute("subject", subjectService.findOne(id).get());
        model.addAttribute("students", subjectService.findOne(id).get().getEnrolledStudents());
        return "admin/showsubject";
    }

    @GetMapping("/subjects/new")
    public String newSub(Model model) {
        model.addAttribute("subject", new Subject());
        return "admin/newsubject";
    }

    @PostMapping("/subjects/new")
    public String newSubjectAdd(@ModelAttribute("subject") Subject subject) {
        subjectService.save(subject);
        return "redirect:/admin/subjects";
    }

    @DeleteMapping("/subjects/{id}")
    public String deleteSubject(@PathVariable("id") int id) {
        subjectService.delete(id);
        return "admin/allsubjects";
    }

    //TODO
    @GetMapping("/teachers")
    public String allTeachers(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        return "admin/allteachers";
    }

    @GetMapping("/teachers/{id}")
    public String showTeacher(@PathVariable("id") int id, Model model) {
        Teacher teacher = teacherService.findOne(id);
        model.addAttribute("teacher", teacher);
        boolean flag = teacher.getSubjects().isEmpty();
        model.addAttribute("subject" , new Subject());
        model.addAttribute("flag" , flag);
        model.addAttribute("allsubjects" , gettingSubjectsForTeacher(id));
        return "admin/showteacher";
    }

    @GetMapping("/teachers/{id}/edit")
    public String editing(@PathVariable("id") int id, Model model) {
        model.addAttribute("teacher", teacherService.findOne(id));
        return "admin/editteacher";
    }
    @PostMapping("/teachers/{id}/setsubject")
    public String setsubject(@PathVariable("id") int id, @ModelAttribute("subject") Subject subject){
        teacherService.setTeacher(teacherService.findOne(id) , subjectService.findOne(subject.getId()).get());
        System.out.println(subject.getId()+"IDIDIDDIIDIDIIDIDIIDID");
        return "redirect:/admin/teachers"+"/"+id;
    }
    @PatchMapping("/teachers/{id}")
    public String submitEditTeacher(@ModelAttribute("teacher") @Valid Teacher teacher, BindingResult bind,
                                    @PathVariable("id") int id) {
        if (bind.hasErrors())
            return "admin/editteacher";

        teacherService.update(teacher, id);
        return "redirect:/admin/teachers" + "/" + id;
    }

    @GetMapping("/teachers/new")
    public String newTeacher(Model model) {
        model.addAttribute("teacher", new Teacher());
        return "admin/newteacher";
    }

    @PostMapping("/teachers/new")
    public String newTeacherAction(@ModelAttribute("teacher") @Valid Teacher teacher, BindingResult bind) {
        if (bind.hasErrors()) {
            return "admin/newteacher";
        }
        System.out.println(teacher.getName());
        teacherService.save(teacher);
        return "redirect:/admin/teachers";
    }

    @DeleteMapping("/teachers/{id}")
    public String deleteTeacher(@PathVariable("id") int id) {
        teacherService.delete(id);
        return "redirect:/admin/teachers";
    }

    public List<Subject> gettingSubjects(int id) {
        List<Subject> result = new ArrayList<>();
        Student student = studentService.findOne(id);
        Set<Subject> subjectsOfStudent = student.getSubjects();
        List<Subject> temp = new ArrayList<>();
        temp.addAll(subjectsOfStudent);
        List<Subject> allSubjects = subjectService.findAll();
        for (Subject all : allSubjects) {
            if (!temp.contains(all)) {
                result.add(all);
            }
        }
        return result;
    }

    public List<Subject> gettingSubjectsForTeacher(int id) {
        List<Subject> result = new ArrayList<>();
        Teacher teacher = teacherService.findOne(id);
        Set<Subject> subjectsOfStudent = teacher.getSubjects();
        List<Subject> temp = new ArrayList<>();
        temp.addAll(subjectsOfStudent);
        List<Subject> allSubjects = subjectService.findAll();
        for (Subject all : allSubjects) {
            if (!temp.contains(all)) {
                result.add(all);
            }
        }
        return result;
    }
}
