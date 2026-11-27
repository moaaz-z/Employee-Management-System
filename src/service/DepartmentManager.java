package service;

import model.Department;
import storage.DataStorage;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DepartmentManager {
    private  Map<Integer, Department> departments = new HashMap<>();
    private  DataStorage storage;

    public DepartmentManager(DataStorage storage) {
        this.departments=storage.loadDepartments();
        this.storage=storage;
    }

    public void addDepartment(int id, String name) {
        if (name == null || name.trim().isEmpty() || isDuplicate((id))) {
            throw new IllegalArgumentException("Enter Valid Parameters( ID , Name )");
        }
        departments.put(id, new Department(id, name.trim()));
        storage.saveDepartments(departments);


    }

    public Department getDepartment(int id) {
        return departments.get(id);
    }

    public boolean deleteDepartment(int id) {
         departments.remove(id) ;
        storage.saveDepartments(departments);
        return !departments.containsKey(id);
    }

    public boolean isDuplicate(int id) {
        return departments.containsKey(id);
    }

    public Collection<Department> getAllDepartments() {
        return Collections.unmodifiableCollection(departments.values());
    }
}
