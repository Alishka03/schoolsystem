package com.example.schoolsystem.teacher;

import com.example.schoolsystem.subject.Subject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Teacher")
public class Teacher {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "email")
    private String email;
    @Column(name = "year")
    private int year;
    @OneToMany(mappedBy = "teacher")
    private Set<Subject> subjects;
    public Teacher() {
    }

    public Teacher(int id, String name, String lastname, String email, int year, Set<Subject> subjects) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.year = year;
        this.subjects = subjects;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Subject> getSubjects() {
        return this.subjects;
    }

    @Override
    public String toString() {
        return name + " "+lastname+" ,email:"+email;
    }
}
