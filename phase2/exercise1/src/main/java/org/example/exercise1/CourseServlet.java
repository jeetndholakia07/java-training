package org.example.exercise1;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exercise1.dao.CourseDAO;
import org.example.exercise1.dao.CourseDAOImpl;
import org.example.exercise1.dao.StudentDAO;
import org.example.exercise1.dao.StudentDAOImpl;
import org.example.exercise1.models.Student;
import org.example.exercise1.services.CourseService;

import java.io.IOException;
import java.util.List;

@WebServlet("/course")
public class CourseServlet extends HttpServlet {
    private static final String VIEW_PATH = "/WEB-INF/views";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String view = VIEW_PATH + "/students.jsp";
        String action = req.getParameter("action");
        if("addCourse".equals(action)){
            CourseDAO courseDAO = new CourseDAOImpl();
            CourseService courseService = new CourseService(courseDAO);
            courseService.addCourse(req, resp);
        }
        StudentDAO studentDAO = new StudentDAOImpl();
        List<Student> studentList = studentDAO.getAllStudents();
        req.setAttribute("studentList", studentList);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(view);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String view = VIEW_PATH + "/course.jsp";
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(view);
        requestDispatcher.forward(req, resp);
    }
}
