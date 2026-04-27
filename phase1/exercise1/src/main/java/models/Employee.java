package models;

public class Employee {
    private int id;
    private String name;
    private String number;
    private Double salary;

    public Employee(){}

    public Employee(String name, String number,Double salary){
        this.name = name;
        this.number = number;
        this.salary = salary;
    }

    public Employee(int id, String name, String number,Double salary){
        this.id = id;
        this.name = name;
        this.number = number;
        this.salary = salary;
    }

    public Employee(int id, String number, Double salary){
        this.id = id;
        this.number = number;
        this.salary = salary;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return  name;
    }

    public String getNumber(){
        return number;
    }

    public Double getSalary(){
        return salary;
    }

    @Override
    public String toString() {
        return String.format(
        "Employee Details:\nId=%d,%nName=%s,%nNumber=%s,%nSalary=%.2f",
        id, name, number, salary
        );
    }
}
