package storage;

import model.Department;
import model.Employee;

import java.util.ArrayList;
import java.util.Map;

public interface DataStorage {

    void saveEmployees(ArrayList<Employee> list);

    ArrayList<Employee> loadEmployees();

    void saveDepartments(Map<Integer, Department> map);

    Map<Integer, Department> loadDepartments();


}
