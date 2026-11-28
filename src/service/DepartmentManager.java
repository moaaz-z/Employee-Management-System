package service;

import model.Department;
import storage.DataStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DepartmentManager {

    private final Map<Integer, Department> departments;
    private final DataStorage storage;

    public DepartmentManager(DataStorage storage) {
        this.storage = storage;
        Map<Integer, Department> loaded = storage.loadDepartments();
        // A missing or unreadable file must not crash the program
        this.departments = (loaded != null) ? new HashMap<>(loaded) : new HashMap<>();
    }

    public void addDepartment(int id, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Department name is required.");
        }
        if (isDuplicate(id)) {
            throw new IllegalArgumentException("Department ID " + id + " already exists.");
        }
        if (isNameDuplicate(name)) {
            throw new IllegalArgumentException("Department name \"" + name.trim() + "\" already exists.");
        }
        departments.put(id, new Department(id, name.trim()));
        storage.saveDepartments(departments);
    }

    public Department getDepartment(int id) {
        return departments.get(id);
    }
    /** @return true only if a department was actually removed. */
    public boolean deleteDepartment(int id) {
        boolean removed = departments.remove(id) != null;
        if (removed) {
            storage.saveDepartments(departments);
        }
        return removed;
    }

    public boolean isDuplicate(int id) {
        return departments.containsKey(id);
    }

    //Duplicate department names are not allowed, ignoring letter case. 
    public boolean isNameDuplicate(String name) {
        return getDepartmentByName(name) != null;
    }

    public Collection<Department> getAllDepartments() {
        return Collections.unmodifiableCollection(departments.values());
    }
    public String[] getDepartmentNames() {
    return departments.values().stream().map(Department::getDepartmentName).toArray(String[]::new);
}

public Department getDepartmentByName(String name) {
    for (Department d : departments.values()) {
        if (d.getDepartmentName().equals(name)) {
            return d;
        }
    }
    return null;
}
}