package model;

public class FullTimeEmployee extends Employee{
 private double basicSalary;
 private double bonus;
public static final String TYPE = "Full-Time";
    public FullTimeEmployee(int emp_ID, String fullname, int age, String position, Department department, double basicSalary, double bonus) {
        super(emp_ID, fullname, age, position, department);
        if(basicSalary <=0 || bonus < 0){
            throw new IllegalArgumentException("Invalid Operation , Please Enter Valid ( Salary / Bonus )");
        }
        this.basicSalary = basicSalary;
        this.bonus = bonus;
    }

    @Override
    public double calculateSalary(){

            return basicSalary+bonus;
    }
    @Override
    public String getType(){

        return TYPE;             // I think this is bad coding 
                                    //بذمه اهلك مرجعلي التايب استرينج 
                                    //انا لله وانا اليه راجعون

    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }
}
