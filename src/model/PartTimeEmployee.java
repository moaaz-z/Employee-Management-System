package model;

public class PartTimeEmployee extends Employee{
    private double hourlyRate;
    private double hoursWorked;

    public PartTimeEmployee(int emp_ID, String fullname, int age, String position, Department department, double hourlyRate, double hoursWorked) {
        if(hourlyRate <=0 || hoursWorked < 0){
            throw new IllegalArgumentException("Please Enter Valid ( Hourly Rate / Hours Worked )");
        }
        super(emp_ID, fullname, age, position, department);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    @Override
    public String getType(){
        return "Part-Time";    // bad coding
    }

    @Override
    public double calculateSalary() {

        return hoursWorked*hourlyRate;
    }
}
