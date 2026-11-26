package model;

import java.io.Serializable;

public class Department implements Serializable {
    private final int departmentId;
    private final DepartmentType departmentType;

    public Department(int departmentId, DepartmentType departmentType) {
        this.departmentId = departmentId;
        this.departmentType = departmentType;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public DepartmentType getDepartmentType() {
        return departmentType;
    }

    @Override
    public String toString() {
        return departmentType + " (ID: " + departmentId + ")";
    }
}
