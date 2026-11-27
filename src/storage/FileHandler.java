package storage;

import model.Department;
import model.Employee;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileHandler implements DataStorage {
    private String employeeFile;
    private String departmentFile;

    public FileHandler(String employeeFile, String departmentFile) {
        this.employeeFile = employeeFile;
        this.departmentFile = departmentFile;
    }

    @Override
    public void saveEmployees(ArrayList<Employee> list){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(employeeFile))) {
            out.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<Employee> loadEmployees(){
        File file = new File(employeeFile);
        if (!file.exists()) {
            return new ArrayList<Employee>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(employeeFile))) {
        ArrayList<Employee> employees = (ArrayList<Employee>) in.readObject();
            return employees;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<Employee>();
    }

    @Override
    public void saveDepartments(Map<Integer, Department> map){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(departmentFile))) {
            out.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    @SuppressWarnings("unchecked")
    public Map<Integer, Department> loadDepartments(){
        File file = new File(departmentFile);
        if (!file.exists()) {
            return new HashMap<Integer, Department>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(departmentFile))) {
            HashMap<Integer, Department> departments = (HashMap<Integer, Department>) in.readObject();
            return departments;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new HashMap<Integer, Department>();

    }
}
