package model;

public class PartTimeEmployee extends Employee {

    public static final String TYPE = "Part-Time";

    private double hourlyRate;
    private double hoursWorked;

    public PartTimeEmployee(int empId, String fullName, int age, String position,
                            Department department, double hourlyRate, double hoursWorked) {
        super(empId, fullName, age, position, department); // super(...) must be the first statement
        validate(hourlyRate, hoursWorked);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    private static void validate(double hourlyRate, double hoursWorked) {
        if (hourlyRate < 0 || hoursWorked < 0) {
            throw new IllegalArgumentException("Hourly rate and hours worked must be non-negative.");
        }
    }

    @Override
    public double calculateSalary() {
        return hourlyRate * hoursWorked;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        validate(hourlyRate, this.hoursWorked);
        this.hourlyRate = hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        validate(this.hourlyRate, hoursWorked);
        this.hoursWorked = hoursWorked;
    }
}