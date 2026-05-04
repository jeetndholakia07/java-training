package org.example.exercise1.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exercise1.dao.CourseDAO;
import org.example.exercise1.models.Course;

import java.io.IOException;

public class CourseService {
    private final CourseDAO courseDAO;

    public CourseService(CourseDAO courseDAO){
        this.courseDAO = courseDAO;
    }

    public void addCourse(HttpServletRequest request) throws IOException {
        Course c = courseDAO.getCourseByName(request.getParameter("courseName"));
        if(c!=null){
           throw new RuntimeException("Course already exists.");
        }
        Course course = new Course();
        course.setCourseName(request.getParameter("courseName"));
        courseDAO.createCourse(course);
    }
}
