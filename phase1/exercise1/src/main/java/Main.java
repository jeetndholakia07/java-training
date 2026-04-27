import dao.*;
import services.EmployeeService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        try (Scanner sc = new Scanner(System.in)) {
            EmployeeDAO dao = new EmployeeDAOImpl();
            EmployeeService employeeService = new EmployeeService(dao);
            while (true) {
                try {
                    System.out.println("\n1. Add Employee");
                    System.out.println("2. View All Employees");
                    System.out.println("3. View Employee By ID");
                    System.out.println("4. Update Employee");
                    System.out.println("5. Delete Employee");
                    System.out.println("6. Exit");

                    System.out.print("Choose: ");
                    int choice = sc.nextInt();
                    sc.nextLine();
                    switch (choice) {
                        case 1:
                            employeeService.AddEmployee(sc);
                            break;

                        case 2:
                            employeeService.printEmployees();
                            break;

                        case 3:
                            employeeService.printEmployee(sc);
                            break;

                        case 4:
                            employeeService.updateEmployee(sc);
                            break;

                        case 5:
                            employeeService.removeEmployee(sc);
                            break;

                        case 6:
                            System.exit(0);
                    }
                } catch (NullPointerException | IllegalArgumentException | InputMismatchException e) {
                    System.out.println("Error running program: ");
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }
}
