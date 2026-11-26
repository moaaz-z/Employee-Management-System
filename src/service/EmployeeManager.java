package service;

import model.Employee;
import storage.DataStorage;

import java.util.ArrayList;

public class EmployeeManager implements DataStorage {
    private ArrayList<Employee> employees;
    private DataStorage storage;

    public EmployeeManager(ArrayList<Employee> employees, DataStorage storage) {
        this.employees = employees;
        this.storage = storage;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }


}
