import model.Department;
import model.Employee;
import model.FullTimeEmployee;
import model.PartTimeEmployee;
import service.DepartmentManager;
import service.EmployeeManager;
import storage.DataStorage;
import storage.FileHandler;

import java.io.File;

public class TestMain {
    public static void main(String[] args) {
        String empFile = "test_employees.dat";
        String deptFile = "test_departments.dat";

        // Clean up any previous test files
        new File(empFile).delete();
        new File(deptFile).delete();

        DataStorage storage = new FileHandler(empFile, deptFile);

        System.out.println("=== 1. Testing DepartmentManager ===");
        DepartmentManager deptManager = new DepartmentManager(storage);
        deptManager.addDepartment(1, "IT");
        deptManager.addDepartment(2, "Human Resources");
        deptManager.addDepartment(3, "Finance");

        System.out.println("Departments added: " + deptManager.getAllDepartments());
        System.out.println("Get ID 1: " + deptManager.getDepartment(1));
        System.out.println("Is ID 2 duplicate? " + deptManager.isDuplicate(2));

        // Test duplicate department rejection
        try {
            deptManager.addDepartment(1, "Duplicate IT");
            System.out.println("FAIL: Duplicate department should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: Duplicate department rejected -> " + e.getMessage());
        }

        // Test invalid department name rejection
        try {
            deptManager.addDepartment(99, "   ");
            System.out.println("FAIL: Blank department name should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: Blank department rejected -> " + e.getMessage());
        }

        System.out.println("\n=== 2. Testing EmployeeManager ===");
        EmployeeManager empManager = new EmployeeManager(storage);
        Department it = deptManager.getDepartment(1);
        Department hr = deptManager.getDepartment(2);

        Employee e1 = empManager.addFullTimeEmployee("Alice Smith", 28, "Software Engineer", it, 5000, 1000);
        Employee e2 = empManager.addPartTimeEmployee("Bob Jones", 22, "HR Intern", hr, 25, 80);
        System.out.println("Added: " + e1);
        System.out.println("Added: " + e2);
        System.out.println("In-memory employees count: " + empManager.getEmployees().size());

        // Test invalid employee arguments
        try {
            empManager.addPartTimeEmployee("Too Young", 16, "Intern", it, 15, 10);
            System.out.println("FAIL: Age < 18 should have thrown an exception");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: Underage employee rejected -> " + e.getMessage());
        }

        System.out.println("\n=== 3. Testing FileHandler Persistence (Reload from Disk) ===");
        DataStorage reloadedStorage = new FileHandler(empFile, deptFile);
        DepartmentManager reloadedDeptManager = new DepartmentManager(reloadedStorage);
        EmployeeManager reloadedEmpManager = new EmployeeManager(reloadedStorage);

        System.out.println("Reloaded Departments: " + reloadedDeptManager.getAllDepartments());
        System.out.println("Reloaded Employees (" + reloadedEmpManager.getEmployees().size() + "):");
        for (Employee emp : reloadedEmpManager.getEmployees()) {
            System.out.println("  " + emp + " | Salary: " + emp.calculateSalary());
        }

        // Verify nextID continues properly after reload
        Employee e3 = reloadedEmpManager.addFullTimeEmployee("Eve Adams", 30, "Analyst", it, 4500, 500);
        System.out.println("Next auto-ID after reload (expected 3): " + e3.getEmp_ID());

        System.out.println("\n=== 4. Testing Model Classes Directly ===");
        FullTimeEmployee ft = new FullTimeEmployee(100, "Charlie", 35, "Lead", it, 7000, 1500);
        PartTimeEmployee pt = new PartTimeEmployee(101, "Dana", 24, "Consultant", hr, 40, 20);
        System.out.println("FT Salary (expected 8500.0): " + ft.calculateSalary() + " | Type: " + ft.getType());
        System.out.println("PT Salary (expected 800.0): " + pt.calculateSalary() + " | Type: " + pt.getType());

        System.out.println("\n=== 5. Testing Delete Department Persistence ===");
        boolean deleted = deptManager.deleteDepartment(3);
        System.out.println("Deleted dept ID 3: " + deleted);
        DepartmentManager afterDeleteDeptManager = new DepartmentManager(new FileHandler(empFile, deptFile));
        System.out.println("Reloaded Departments after delete: " + afterDeleteDeptManager.getAllDepartments());

        // Clean up test files
        new File(empFile).delete();
        new File(deptFile).delete();
    }
}
