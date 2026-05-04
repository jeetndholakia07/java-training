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
import org.example.exercise1.services.StudentService;
import org.example.exercise1.utils.Toast;

import java.io.IOException;
import java.util.List;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {
    private static final String VIEW_PATH = "/WEB-INF/views";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        StudentDAO studentDAO = new StudentDAOImpl();
        CourseDAO courseDAO = new CourseDAOImpl();
        StudentService studentService = new StudentService(studentDAO, courseDAO);
        try {
            if ("addStudent".equals(action)) {
                studentService.addStudent(req);
                Toast.setMessage(req,"success", "Student created successfully!");
            } else if ("editStudent".equals(action)) {
                studentService.editStudent(req);
                Toast.setMessage(req,"success", "Student updated successfully!");
            } else if ("deleteStudent".equals(action)) {
                studentService.softDeleteStudent(req);
                Toast.setMessage(req,"success", "Student deleted successfully!");
            }
            resp.sendRedirect("students");
        } catch (Exception e) {
            Toast.setMessage(req,"error", e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        StudentDAO studentDAO = new StudentDAOImpl();
        CourseDAO courseDAO = new CourseDAOImpl();

        if ("edit".equals(action)) {
            String studentId = req.getParameter("id");
            if (studentId == null) {
               Toast.setMessage(req, "error", "Student ID is missing");
               resp.sendRedirect("students");
               return;
            }
            Student student = studentDAO.getStudentById(Integer.parseInt(studentId));
            if (student == null) {
                Toast.setMessage(req, "error", "Student not found");
                resp.sendRedirect("students");
                return;
            }
            req.setAttribute("student", student);
            req.setAttribute("courseList", courseDAO.getAllCourses());
            req.setAttribute("studentCourses", studentDAO.getCoursesForStudent(student.getId()));
            RequestDispatcher rd = req.getRequestDispatcher(VIEW_PATH + "/edit-student.jsp");
            rd.forward(req, resp);
            return;
        }
        List<Student> studentList = studentDAO.getAllStudents();
        req.setAttribute("studentList", studentList);
        RequestDispatcher rd = req.getRequestDispatcher(VIEW_PATH + "/students.jsp");
        rd.forward(req, resp);
    }
}