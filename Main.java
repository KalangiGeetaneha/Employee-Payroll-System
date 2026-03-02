import java.util.*;
import java.io.*;

// Abstract Employee class
abstract class Employee {
    private String name;
    private int id;

    public Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

   
    public abstract double calculateSalary();

    public abstract String getType();

    public void display() {
        System.out.println("Employee ID: " + id);
        System.out.println("Employee Name: " + name);
        System.out.println("Type: " + getType());
        System.out.println("Salary: " + calculateSalary());
    }
}

// Full Time Employee
class FullTimeEmployee extends Employee {

    private double salary;
    private double bonus;

    public FullTimeEmployee(String name, int id, double salary, double bonus) {
        super(name, id);
        this.salary = salary;
        this.bonus = bonus;
    }
    
    @Override
    public double calculateSalary() {
        return salary + bonus;
    }

    @Override
    public String getType() {
        return "FullTime";
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public double getBonus() {
        return bonus;
    }
    
    @Override
    public void display() {
        super.display();
        System.out.println("Base Salary: " + salary);
        System.out.println("Bonus: " + bonus);
    }
}

// Part Time Employee
class PartTimeEmployee extends Employee {

    private int hoursWorked;
    private double ratePerHour;

    public PartTimeEmployee(String name, int id, int hoursWorked, double ratePerHour) {
        super(name, id);
        this.hoursWorked = hoursWorked;
        this.ratePerHour = ratePerHour;
    }

    @Override
    public double calculateSalary() {
        return hoursWorked * ratePerHour;
    }
    
    @Override
    public String getType() {
        return "PartTime";
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public double getRatePerHour() {
        return ratePerHour;
    }

    @Override 
    public void display() {
        super.display();
        System.out.println("Hours Worked: " + hoursWorked);
        System.out.println("Rate Per Hour: " + ratePerHour);
    }
}

// Payroll System class
class PayrollSystem {

    private List<Employee> employeeList = new ArrayList<>();

    // Add employee with duplicate ID check
    public void addEmployee(Employee emp) {

        for (Employee e : employeeList) {
            if (e.getId() == emp.getId()) {
                System.out.println("Employee with this ID already exists.");
                return;
            }
        }

        employeeList.add(emp);
        System.out.println("Employee added successfully");
    }

    // Remove employee
    public void removeEmployee(int id) {

        Iterator<Employee> it = employeeList.iterator();

        while (it.hasNext()) {

            Employee emp = it.next();

            if (emp.getId() == id) {
                it.remove();
                System.out.println("Employee removed successfully");
                return;
            }
        }

        System.out.println("Employee not found");
    }

    // Search employee
    public void searchEmployee(int id) {

        for (Employee emp : employeeList) {
            if (emp.getId() == id) {
                emp.display();
                return;
            }
        }

        System.out.println("Employee not found");
    }

    // Display all employees
    public void displayEmployees() {

        if (employeeList.isEmpty()) {
            System.out.println("No employees available");
            return;
        }

        for (Employee emp : employeeList) {
            System.out.println("----------------------");
            emp.display();
        }
    }

    // Update salary
    public void updateEmployee(int id, double newSalary) {

        for (Employee emp : employeeList) {

            if (emp.getId() == id) {

                if (emp instanceof FullTimeEmployee) {

                    ((FullTimeEmployee) emp).setSalary(newSalary);

                    System.out.println("Salary updated successfully");
                    return;
                }
                else {
                    System.out.println("Only Full Time employees can update salary");
                    return;
                }
            }
        }

        System.out.println("Employee not found");
    }

    // Calculate total payroll
    public void calculateTotalPayroll() {

        double total = 0;

        for (Employee emp : employeeList) {
            total += emp.calculateSalary();
        }

        System.out.println("Total Payroll Expense: " + total);
    }

    // Show employee count
    public void showEmployeeCount() {
        System.out.println("Total Employees: " + employeeList.size());
    }

    // Save employees to file
    public void saveEmployees() {

        try {

            FileWriter fw = new FileWriter("employees.txt");

            for (Employee emp : employeeList) {

                if (emp instanceof FullTimeEmployee) {

                    FullTimeEmployee f = (FullTimeEmployee) emp;

                    fw.write(f.getId() + "," + f.getName() + ",FullTime," + f.getSalary() + "," + f.getBonus() + "\n");

                } else if (emp instanceof PartTimeEmployee) {

                    PartTimeEmployee p = (PartTimeEmployee) emp;

                    fw.write(p.getId() + "," + p.getName() + ",PartTime," + p.getHoursWorked() + "," + p.getRatePerHour() + "\n");
                }
            }

            fw.close();

            System.out.println("Employees saved successfully");

        } catch (IOException e) {
            System.out.println("Error saving employees");
        }
    }

    // Load employees from file
    public void loadEmployees() {

        try {

            File file = new File("employees.txt");

            if (!file.exists())
                return;

            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();

                String[] data = line.split(",");

                int id = Integer.parseInt(data[0]);
                String name = data[1];
                String type = data[2];

                if (type.equals("FullTime")) {

                    double salary = Double.parseDouble(data[3]);
                    double bonus = Double.parseDouble(data[4]);

                    employeeList.add(new FullTimeEmployee(name, id, salary, bonus));

                } else {

                    int hours = Integer.parseInt(data[3]);
                    double rate = Double.parseDouble(data[4]);

                    employeeList.add(new PartTimeEmployee(name, id, hours, rate));
                }
            }

            fileScanner.close();

        } catch (Exception e) {
            System.out.println("Error loading employees");
        }
    }
}

// Main class
public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        PayrollSystem payroll = new PayrollSystem();

        payroll.loadEmployees();

        while (true) {

            System.out.println("\n===== Employee Payroll System =====");
            System.out.println("1 Add Full Time Employee");
            System.out.println("2 Add Part Time Employee");
            System.out.println("3 Display Employees");
            System.out.println("4 Search Employee");
            System.out.println("5 Remove Employee");
            System.out.println("6 Update Salary");
            System.out.println("7 Total Payroll Expense");
            System.out.println("8 Save Employees");
            System.out.println("9 Employee Count");
            System.out.println("10 Exit");

            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:

                    sc.nextLine();

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();

                    System.out.print("Enter Salary: ");
                    double salary = sc.nextDouble();

                    System.out.print("Enter Bonus: ");
                    double bonus = sc.nextDouble();

                    payroll.addEmployee(new FullTimeEmployee(name, id, salary, bonus));
                    break;

                case 2:

                    sc.nextLine();

                    System.out.print("Enter Name: ");
                    String pname = sc.nextLine();

                    System.out.print("Enter ID: ");
                    int pid = sc.nextInt();

                    System.out.print("Enter Hours Worked: ");
                    int hours = sc.nextInt();

                    System.out.print("Enter Rate Per Hour: ");
                    double rate = sc.nextDouble();

                    payroll.addEmployee(new PartTimeEmployee(pname, pid, hours, rate));
                    break;

                case 3:
                    payroll.displayEmployees();
                    break;

                case 4:
                    System.out.print("Enter Employee ID: ");
                    payroll.searchEmployee(sc.nextInt());
                    break;

                case 5:
                    System.out.print("Enter Employee ID to remove: ");
                    payroll.removeEmployee(sc.nextInt());
                    break;

                case 6:

                    System.out.print("Enter Employee ID: ");
                    int uid = sc.nextInt();

                    System.out.print("Enter New Salary: ");
                    double newSalary = sc.nextDouble();

                    payroll.updateEmployee(uid, newSalary);
                    break;

                case 7:
                    payroll.calculateTotalPayroll();
                    break;

                case 8:
                    payroll.saveEmployees();
                    break;

                case 9:
                    payroll.showEmployeeCount();
                    break;

                case 10:

                    payroll.saveEmployees();

                    System.out.println("Exiting... Data saved.");

                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}