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
import org.example.exercise1.models.Course;
import org.example.exercise1.models.Student;
import org.example.exercise1.services.CourseService;
import org.example.exercise1.services.StudentService;

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
        if("addStudent".equals(action)){
            studentService.addStudent(req, resp);
        } else if ("editStudent".equals(action)) {
            studentService.editStudent(req, resp);
            resp.sendRedirect("students");
            return;
        } else if ("deleteStudent".equals(action)) {
            studentService.softDeleteStudent(req);
        }
        String view = VIEW_PATH + "/students.jsp";
        List<Student> studentList = studentDAO.getAllStudents();
        req.setAttribute("studentList", studentList);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(view);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        System.out.println(req.getContextPath());
        StudentDAO studentDAO = new StudentDAOImpl();
        String view = "";
        if("edit".equals(action)){
            String studentId = req.getParameter("id");
            if(studentId!=null){
                Student student = studentDAO.getStudentById(Integer.parseInt(studentId));
                if(student!=null){
                    req.setAttribute("student", student);
                    view = VIEW_PATH + "/edit-student.jsp";
                }
                else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Student not found");
                    return;
                }
            }
            else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Student ID is missing");
                return;
            }
        }
        else{
            view = VIEW_PATH + "/students.jsp";
            List<Student> studentList = studentDAO.getAllStudents();
            req.setAttribute("studentList", studentList);
        }
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(view);
        requestDispatcher.forward(req, resp);
    }
}