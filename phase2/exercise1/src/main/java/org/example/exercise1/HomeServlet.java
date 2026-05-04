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
    private final CourseDAO courseDAO = new CourseDAOImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("courseList", courseDAO.getAllCourses());
        RequestDispatcher rd = req.getRequestDispatcher(VIEW_PATH + "/index.jsp");
        rd.forward(req, resp);
    }
}
