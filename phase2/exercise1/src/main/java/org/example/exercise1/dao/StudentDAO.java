package org.example.exercise1.dao;

import org.example.exercise1.models.Student;

import java.util.List;

public interface StudentDAO {
    public void createStudent(Student student);
    public Student getStudentByName(String name);
    public List<Student> getAllStudents();
    public List<Student> getStudentsById(List<Integer> ids);
    public Student getStudentById(int id);
    public void updateStudent(Student student);
    public void deleteStudent(int id);
}
