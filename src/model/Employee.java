package model;

import java.io.Serializable;

public abstract class Employee implements Serializable {
    private final int emp_ID;           // Cannot be altered
    private final String fullName;      // Cannot be altered
    private int age;
    private Department department;
    private String position;


    public Employee(int emp_ID, String fullName, int age, String position, Department department) {


        this.emp_ID = emp_ID;
        this.fullName = fullName;
        this.age = age;
        this.position = position;
        this.department = department;
    }

    public int getEmp_ID() {
        return emp_ID;
    }

    public String getFullname() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    public abstract double calculateSalary();

    public abstract String getType();

    @Override
    public String toString() {
        return "Employee ID: " + emp_ID +
                ", Name: " + fullName +
                ", Age: " + age +
                ", Position: " + position +
                ", Department: " + department +
                ", Type: " + getType();
    }


}
