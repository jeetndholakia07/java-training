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

    public void addCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Course c = courseDAO.getCourseByName(request.getParameter("courseName"));
        if(c!=null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course already exists.");
            return;
        }
        Course course = new Course();
        course.setCourseName(request.getParameter("courseName"));
        courseDAO.createCourse(course);
    }

    public void editCourse(HttpServletRequest request){
        Course course = courseDAO.getCourseById(Integer.parseInt(request.getParameter("id")));
        courseDAO.updateCourse(course);
    }
}
