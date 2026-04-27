package dao;
import models.Employee;
import config.DBConnection;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import utils.Validator;

public class EmployeeDAOImpl implements EmployeeDAO{
    @Override
    public void createEmployee(Employee emp) {
        String select = "SELECT name FROM Employees WHERE name = ?";
        String sql = "INSERT INTO Employees(name, number, salary) VALUES(?,?,?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            PreparedStatement checkStmt = conn.prepareStatement(select);
        )
        {
            checkStmt.setString(1, emp.getName());
            try(ResultSet rs = checkStmt.executeQuery()){
                if(rs.next()){
                    System.out.println("Employee already exists.");
                    return;
                };
                ps.setString(1, emp.getName());
                ps.setString(2, emp.getNumber());
                ps.setDouble(3, emp.getSalary());
                ps.executeUpdate();
                System.out.println("Employee added.");
            }
        }
            catch (SQLException e){
            System.out.println("Error inserting employee:");
            e.printStackTrace();
        }
    }

    @Override
    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employees";
        try(Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        )
        {
            while(rs.next()){
                employees.add(new Employee(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("number"),
                rs.getDouble("salary")
            ));
            }
        }
        catch (SQLException e){
            System.out.println("Error fetching employees: ");
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public Employee getEmployeeById(int id) {
        String sql = "SELECT * FROM Employees WHERE id=?";
        Employee emp = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
            ) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery();){
                if (rs.next()) {
                    emp = new Employee(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("number"),
                            rs.getDouble("salary")
                    );
                }
            }
        }
        catch (SQLException e){
           System.out.println("Error fetching employee by id: ");
            e.printStackTrace();
        }
        return emp;
    }

    @Override
    public void updateEmployee(Employee emp) {
        String sql = "UPDATE Employees SET number=?, salary=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (!Validator.isValidMobile(emp.getNumber())) {
                throw new IllegalArgumentException("Invalid mobile number");
            }
            if(emp.getSalary()<=0){
                throw new IllegalArgumentException("Salary must be positive");
            }
            ps.setString(1, emp.getNumber());
            ps.setDouble(2, emp.getSalary());
            ps.setInt(3, emp.getId());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                System.out.println("Employee not found");
            } else {
                System.out.println("Employee updated");
            }
        }
         catch (SQLException e){
            System.out.println("Error updating employee: ");
            e.printStackTrace();
         }
    }

    @Override
    public void deleteEmployee(int id) {
        String sql = "DELETE FROM Employees WHERE id=?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ){
           ps.setInt(1, id);
           int rows = ps.executeUpdate();
            if (rows == 0) {
                System.out.println("Employee not found");
                return;
            }
           System.out.println("Employee deleted.");
        }
        catch (SQLException e){
           System.out.println("Error deleting employee: ");
            e.printStackTrace();
        }
    }
}
