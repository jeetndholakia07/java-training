package org.example.exercise1;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exercise1.dao.CourseDAO;
import org.example.exercise1.dao.CourseDAOImpl;
import org.example.exercise1.services.CourseService;
import org.example.exercise1.utils.Toast;

import java.io.IOException;

@WebServlet("/course")
public class CourseServlet extends HttpServlet {

    private static final String VIEW_PATH = "/WEB-INF/views";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        CourseDAO courseDAO = new CourseDAOImpl();
        CourseService courseService = new CourseService(courseDAO);
        try {
            if ("addCourse".equals(action)) {
                courseService.addCourse(req);
                Toast.setMessage(req, "success", "Course created successfully!");
            }
        } catch (Exception e) {
            Toast.setMessage(req, "error", e.getMessage());
        }
        resp.sendRedirect("course");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher(VIEW_PATH + "/course.jsp");
        rd.forward(req, resp);
    }
}