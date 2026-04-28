package org.example.exercise1;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exercise1.dao.CourseDAO;
import org.example.exercise1.dao.CourseDAOImpl;

import java.io.IOException;

@WebServlet("/")
public class HomeServlet extends HttpServlet {
    private static final String VIEW_PATH = "/WEB-INF/views";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CourseDAO courseDAO = new CourseDAOImpl();
        req.setAttribute("courseList", courseDAO.getAllCourses());
        String view = VIEW_PATH + "/index.jsp";
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(view);
        requestDispatcher.forward(req, resp);
    }
}
