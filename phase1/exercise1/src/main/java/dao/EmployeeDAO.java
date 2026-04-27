package dao;
import models.Employee;
import java.util.List;

public interface EmployeeDAO {
    void createEmployee(Employee emp);
    List<Employee> getEmployees();
    Employee getEmployeeById(int id);
    void updateEmployee(Employee emp);
    void deleteEmployee(int id);
}
