package org.example.exercise1.dao;

import org.example.exercise1.models.Course;

import java.util.List;

public interface CourseDAO {
    public List<Course> getAllCourses();
    public void createCourse(Course course);
    public Course getCourseById(int id);
    public Course getCourseByName(String name);
    public List<Course> getCoursesById(List<Integer> ids);
}
