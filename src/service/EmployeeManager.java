package service;

import model.Department;
import model.Employee;
import model.FullTimeEmployee;
import model.PartTimeEmployee;
import storage.DataStorage;

import java.util.ArrayList;

public class EmployeeManager {
    private ArrayList<Employee> employees;
    private DataStorage storage;
    private int nextID = 1;

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public EmployeeManager(DataStorage storage) {
        this.employees = storage.loadEmployees();
        int maxID = 0;
        for (Employee e : employees) {
            if (e.getEmp_ID() > maxID) {
                maxID = e.getEmp_ID();
            }
        }
        nextID = maxID + 1;
        this.storage = storage;
    }

    public Employee addFullTimeEmployee(String fullName, int age, String position, Department department, double basicSalary, double bonus) {

        if (fullName.isEmpty() || age < 18 || position.isEmpty() || department == null || basicSalary <= 0 || bonus < 0) {
            throw new IllegalArgumentException("Please Enter Valid Arguments");
        }

            int id = nextID++;
            Employee emp = new FullTimeEmployee(id, fullName, age, position, department, basicSalary, bonus);
            employees.add(emp);
            storage.saveEmployees(employees);
            return emp;
        }


    public Employee addPartTimeEmployee(String fullName, int age, String position, Department department, double hourlyRate, int hoursWorked) {
        if (fullName.isEmpty() || age < 18 || position.isEmpty() || department == null || hourlyRate <= 0 || hoursWorked < 0) {
            throw new IllegalArgumentException("Please Enter Valid Arguments");
        }
        int id = nextID++;
        Employee emp = new PartTimeEmployee(id, fullName, age, position, department, hourlyRate, hoursWorked);
        employees.add(emp);
        storage.saveEmployees(employees);
        return emp;
    }

}
