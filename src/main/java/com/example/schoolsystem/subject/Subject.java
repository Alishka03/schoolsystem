package com.example.schoolsystem.subject;

import com.example.schoolsystem.student.Student;
import com.example.schoolsystem.teacher.Teacher;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @ManyToMany
    @JoinTable(
            name = "enrolled_students",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    Set<Student> enrolledStudents = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    public void setId(int id) {
        this.id = id;
    }

    public void setEnrolledStudents(Set<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
