# Employee Management System (EMS)

## Description

EMS is a desktop application built with Java Swing and Object-Oriented Programming. It helps a company manage its employees and departments. An administrator can log in, add, view, update, delete, search, and filter employee records, manage departments, and see each employee's calculated salary. All data is saved to files and reloaded when the application starts.

### Employee types and salary rules

| Type | Extra data | Monthly salary |
|------|-----------|----------------|
| Full-Time | basic salary, bonus | basic salary + bonus |
| Part-Time | hourly rate, hours worked | hourly rate x hours worked |

### OOP principles used

- **Encapsulation:** all fields are private and accessed through getters and setters.
- **Inheritance:** `FullTimeEmployee` and `PartTimeEmployee` extend the abstract class `Employee`.
- **Polymorphism:** each subclass overrides `calculateSalary()`, so the right formula runs for each employee without type checks.
- **Abstraction:** `Employee` is abstract and declares the abstract methods `calculateSalary()` and `getType()`. `DataStorage` is an interface that hides how data is stored.

## Features

- Login screen with credential validation and a logout option
- Dashboard with a welcome message and navigation
- Add employee (form fields change with the selected employee type)
- View all employees in a table, with the correct salary for each type
- Update the selected employee
- Delete an employee after a confirmation dialog
- Search by employee ID or name, and filter by department (case-insensitive)
- Department management (no duplicate IDs, no duplicate names ignoring case)
- Input validation with clear error messages
- Automatic saving and loading of employees and departments

## Project Structure

```
src/
  Main.java
  model/     Employee, FullTimeEmployee, PartTimeEmployee, Department
  service/   EmployeeManager, DepartmentManager
  storage/   DataStorage, FileHandler
  gui/       LoginFrame, MainFrame, DashboardPanel, AddEmployeePanel,
             ViewEmployeesPanel, SearchUpdatePanel, DepartmentPanel
data/        employees.dat, departments.dat (created automatically)
docs/        UML class diagram and screenshots
```

The GUI classes only handle user interaction and display. Business rules live in the managers, and all file reading and writing is done by `FileHandler`.

## Requirements

- JDK 11 or newer (check with `java -version` and `javac -version`)

## Compile and Run

Open a terminal in the project folder.

**Linux / macOS**

```
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out Main
```

**Windows (Command Prompt)**

```
mkdir out
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
java -cp out Main
```

You can also open the project in an IDE (IntelliJ IDEA, Eclipse, or VS Code), mark `src` as the source folder, and run `Main`.

## Login Credentials

These fixed demonstration credentials are used for the lab:

- Username: `admin`
- Password: `admin123`

## Data Storage

- **Format:** Java object serialization (`Employee` and `Department` implement `Serializable`). The full objects are saved, so employee types and their salary data are rebuilt correctly when the application restarts.
- **Location:** the `data/` folder in the directory where the application is run.
  - `data/employees.dat`
  - `data/departments.dat`
- On the first launch, when no files exist, the application starts with an empty employee list and the default departments (IT, Human Resources, Finance, Marketing, Sales).
- File errors (missing or unreadable files) are handled with messages and do not close the application.

## Sample Workflow

1. Start the application and log in.
2. Open **Add Employee**, choose Full-Time or Part-Time, enter the details, and save.
3. Open **View Employees** and check the record and its salary.
4. Search for the employee by ID.
5. Select the employee and update the information.
6. Delete an employee and confirm.
7. Close and reopen the application, and check that the records are loaded.
