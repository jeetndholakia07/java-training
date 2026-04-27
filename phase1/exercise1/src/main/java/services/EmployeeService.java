package services;

import dao.*;
import models.Employee;
import utils.Validator;
import java.util.List;
import java.util.Scanner;

public class EmployeeService {
    private EmployeeDAO empDAO;
    public EmployeeService(EmployeeDAO dao){
        this.empDAO = dao;
    }
    public void AddEmployee(Scanner sc) throws IllegalArgumentException{
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Number: ");
        String number = sc.nextLine();
        System.out.print("Salary: ");
        Double salary = sc.nextDouble();
        if (!Validator.isValidMobile(number)) {
            throw new IllegalArgumentException("Invalid mobile number");
        }
        if(salary<=0){
            throw new IllegalArgumentException("Salary must be positive");
        }
        empDAO.createEmployee(new Employee(name, number, salary));
    }

    public void printEmployees(){
        List<Employee> employees = empDAO.getEmployees();
        employees.forEach(e -> System.out.println(e));
    }

    public void printEmployee(Scanner sc){
        System.out.print("Enter ID: ");
        int id = sc.nextInt();

        Employee emp = empDAO.getEmployeeById(id);
        if (emp != null) {
            System.out.println(emp);
        } else {
            System.out.println("Employee not found");
        }
    }

    public void updateEmployee(Scanner sc){
        System.out.print("ID: ");
        int uid = sc.nextInt();
        Employee employee = empDAO.getEmployeeById(uid);
        if (employee == null) {
            System.out.println("Employee not found");
            return;
        }
        sc.nextLine();
        System.out.print("New Number: ");
        String newNumber = sc.nextLine();
        System.out.print("New Salary: ");
        Double newSalary = sc.nextDouble();
        empDAO.updateEmployee(new Employee(uid, newNumber, newSalary));
    }

    public void removeEmployee(Scanner sc){
        System.out.print("ID: ");
        int delId = sc.nextInt();
        empDAO.deleteEmployee(delId);
    }
}
