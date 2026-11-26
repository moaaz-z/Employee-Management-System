package model;

import java.io.Serializable;

public abstract class Employee implements Serializable {
    private final int emp_ID;           // Cannot be altered
    private final String fullname;      // Cannot be altered
    private int age;
    private Department department;
    private String position;


    public Employee(int emp_ID, String fullname, int age, String position, Department department) {
        if(emp_ID < 0 || fullname.isEmpty() || age < 18 || position.isEmpty() || department==null){
            throw new IllegalArgumentException("Please Enter Valid Arguments");
        }

        this.emp_ID = emp_ID;
        this.fullname = fullname;
        this.age = age;
        this.position = position;
        this.department = department;
    }

    public int getEmp_ID() {
        return emp_ID;
    }

    public String getFullname() {
        return fullname;
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
                ", Name: " + fullname +
                ", Age: " + age +
                ", Position: " + position +
                ", Department: " + department +
                ", Type: " + getType();
    }


}
