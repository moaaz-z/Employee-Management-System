package service;

import model.Department;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DepartmentManager {
    private final Map<Integer, Department> departments = new HashMap<>();

    public DepartmentManager() {
        addDepartment(1, "IT");
        addDepartment(2, "Human Resources");
        addDepartment(3, "Finance");
        addDepartment(4, "Marketing");
        addDepartment(5, "Sales");
    }

    public boolean addDepartment(int id, String name) {
        if (name == null || name.trim().isEmpty() || isDuplicate((id))) {
            return false;
        }
        departments.put(id, new Department(id, name.trim()));
        return true;
    }

    public Department getDepartment(int id) {
        return departments.get(id);
    }

    public boolean deleteDepartment(int id) {
        return departments.remove(id) != null;
    }

    public boolean isDuplicate(int id) {
        return departments.containsKey(id);
    }

    public Collection<Department> getAllDepartments() {
        return Collections.unmodifiableCollection(departments.values());
    }
}
