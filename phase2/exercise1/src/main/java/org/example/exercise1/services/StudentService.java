package org.example.exercise1.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exercise1.dao.CourseDAO;
import org.example.exercise1.dao.CourseDAOImpl;
import org.example.exercise1.dao.StudentDAO;
import org.example.exercise1.models.Course;
import org.example.exercise1.models.Student;
import org.example.exercise1.utils.Status;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StudentService {
    private final StudentDAO studentDAO;
    private CourseDAO courseDAO;
    public StudentService(StudentDAO studentDAO, CourseDAO courseDAO){
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
    }
    public void addStudent(HttpServletRequest request, HttpServletResponse response) throws IOException{
        Student student = new Student();
        student = addStudentDetails(request, response, student);
        if (student==null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Student already exists.");
            return;
        }
        student.setStatus(Status.A);
        student.setBirthDate(Date.valueOf(request.getParameter("birthDate")));
        List<Course> courses = getCourses(request);
        student.setCourses(courses);
        studentDAO.createStudent(student);
    }

    public List<Course> getCourses(HttpServletRequest request){
        String[] courseIds = request.getParameterValues("courses");
        List<Course> courses = new ArrayList<>();
        if(courseIds!=null){
            List<Integer> ids = Arrays.stream(courseIds).map(Integer::parseInt).collect(Collectors.toList());
            courses = courseDAO.getCoursesById(ids);
        }
        return courses;
    }

    public Student addStudentDetails(HttpServletRequest request, HttpServletResponse response, Student student) throws IOException {
        String firstName = request.getParameter("firstName");
        String middleName = request.getParameter("middleName");
        String lastName = request.getParameter("lastName");
        String fullName = formatFullName(firstName, middleName, lastName);
        Student s = studentDAO.getStudentByName(fullName);
        if(s!=null){
            return null;
        }
        student.setFirstName(firstName);
        student.setMiddleName(middleName);
        student.setLastName(lastName);
        student.setFullName(fullName);
        return student;
    }

    public void editStudent(HttpServletRequest request, HttpServletResponse response) throws IOException{
        int id = Integer.parseInt(request.getParameter("studentId"));
        Student student = studentDAO.getStudentById(id);
        addStudentDetails(request, response, student);
        student.setBirthDate(Date.valueOf(request.getParameter("birthDate")));
        studentDAO.updateStudent(student);
    }

    public void softDeleteStudent(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("studentId"));
        studentDAO.deleteStudent(id);
    }

    public String formatFullName(String firstName, String middleName, String lastName){
        return String.format("%s %s %s", firstName, middleName, lastName);
    }
}
