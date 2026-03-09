import java.sql.*;
import java.util.*;

// ===================== ABSTRACT EMPLOYEE =====================
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
        System.out.printf("%-10d %-15s %-12s %-10.2f\n",
                id, name, getType(), calculateSalary());
    }


}

// ===================== FULL TIME EMPLOYEE =====================
class FullTimeEmployee extends Employee {


    private double salary;
    private double bonus;

    public FullTimeEmployee(String name, int id, double salary, double bonus) {
        super(name, id);
        this.salary = salary;
        this.bonus = bonus;
    }

    public double calculateSalary() {
        return salary + bonus;
    }

    public String getType() {
        return "FullTime";
    }

    public double getSalary() {
        return salary;
    }

    public double getBonus() {
        return bonus;
    }


}

// ===================== PART TIME EMPLOYEE =====================
class PartTimeEmployee extends Employee {


    private int hoursWorked;
    private double ratePerHour;

    public PartTimeEmployee(String name, int id, int hoursWorked, double ratePerHour) {
        super(name, id);
        this.hoursWorked = hoursWorked;
        this.ratePerHour = ratePerHour;
    }

    public double calculateSalary() {
        return hoursWorked * ratePerHour;
    }

    public String getType() {
        return "PartTime";
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public double getRatePerHour() {
        return ratePerHour;
    }


}

// ===================== DATABASE CONNECTION =====================
class DBConnection {


    static final String URL = "jdbc:mysql://localhost:3306/payroll_db";
    static final String USER = "root";
    static final String PASSWORD = "Girija@1986";

    public static Connection getConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }


}

// ===================== PAYROLL SYSTEM =====================
class PayrollSystem {


    // Add Employee
    public void addEmployee(Employee emp) {

        String query = "INSERT INTO employees VALUES(?,?,?,?,?,?,?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)
        ) {

            ps.setInt(1, emp.getId());
            ps.setString(2, emp.getName());
            ps.setString(3, emp.getType());

            if (emp instanceof FullTimeEmployee) {

                FullTimeEmployee f = (FullTimeEmployee) emp;

                ps.setDouble(4, f.getSalary());
                ps.setDouble(5, f.getBonus());
                ps.setNull(6, Types.INTEGER);
                ps.setNull(7, Types.DOUBLE);

            } else {

                PartTimeEmployee p = (PartTimeEmployee) emp;

                ps.setNull(4, Types.DOUBLE);
                ps.setNull(5, Types.DOUBLE);
                ps.setInt(6, p.getHoursWorked());
                ps.setDouble(7, p.getRatePerHour());
            }

            ps.executeUpdate();
            System.out.println("Employee added successfully.");

        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
        }
    }

    // Display Employees
    public void displayEmployees() {

        System.out.printf("%-10s %-15s %-12s %-10s\n",
                "ID","Name","Type","Salary");

        System.out.println("------------------------------------------------");

        try (
                Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM employees")
        ) {

            while (rs.next()) {

                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");

                if (type.equals("FullTime")) {

                    Employee emp = new FullTimeEmployee(
                            name, id,
                            rs.getDouble("salary"),
                            rs.getDouble("bonus"));

                    emp.display();

                } else {

                    Employee emp = new PartTimeEmployee(
                            name, id,
                            rs.getInt("hoursWorked"),
                            rs.getDouble("ratePerHour"));

                    emp.display();
                }
            }

        } catch (SQLException e) {
            System.out.println("Error displaying employees.");
        }
    }

    // Search Employee
    public void searchEmployee(int id) {

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps =
                        con.prepareStatement("SELECT * FROM employees WHERE id=?")
        ) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String name = rs.getString("name");
                String type = rs.getString("type");

                if (type.equals("FullTime")) {

                    new FullTimeEmployee(name,id,
                            rs.getDouble("salary"),
                            rs.getDouble("bonus")).display();

                } else {

                    new PartTimeEmployee(name,id,
                            rs.getInt("hoursWorked"),
                            rs.getDouble("ratePerHour")).display();
                }

            } else {
                System.out.println("Employee not found.");
            }

        } catch (SQLException e) {
            System.out.println("Search error: " + e.getMessage());
        }
    }

    // Remove Employee
    public void removeEmployee(int id) {

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps =
                        con.prepareStatement("DELETE FROM employees WHERE id=?")
        ) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Employee removed.");
            else
                System.out.println("Employee not found.");

        } catch (SQLException e) {
            System.out.println("Delete error.");
        }
    }

    // Update Salary
    public void updateEmployee(int id, double newSalary) {

        if(newSalary < 0){
            System.out.println("Invalid salary.");
            return;
        }

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(
                                "UPDATE employees SET salary=? WHERE id=? AND type='FullTime'")
        ) {

            ps.setDouble(1, newSalary);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Salary updated.");
            else
                System.out.println("Employee not found or not FullTime.");

        } catch (SQLException e) {
            System.out.println("Update error.");
        }
    }

    // Total Payroll
    public void calculateTotalPayroll() {

        try (
                Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(
                        "SELECT SUM(COALESCE(salary+bonus,hoursWorked*ratePerHour)) AS total FROM employees")
        ) {

            if (rs.next())
                System.out.println("Total Payroll Expense: " + rs.getDouble("total"));

        } catch (SQLException e) {
            System.out.println("Payroll calculation error.");
        }
    }

    // Employee Count
    public void showEmployeeCount() {

        try (
                Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT COUNT(*) AS total FROM employees")
        ) {

            if (rs.next())
                System.out.println("Total Employees: " + rs.getInt("total"));

        } catch (SQLException e) {
            System.out.println("Count error.");
        }
    }

    // Highest Paid Employee
    public void highestPaidEmployee(){

        String sql = """
    SELECT id,name,type,
    COALESCE(salary+bonus,hoursWorked*ratePerHour) AS totalSalary
    FROM employees
    ORDER BY totalSalary DESC
    LIMIT 1
    """;

        try(
                Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)
        ){

            if(rs.next()){
                System.out.println("\nHighest Paid Employee");
                System.out.println("---------------------");

                System.out.printf("%-10d %-15s %-12s %-10.2f\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getDouble("totalSalary"));
            }

        }catch(Exception e){
            System.out.println("Error finding highest salary.");
        }
    }


}

// ===================== MAIN CLASS =====================
public class Main {


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PayrollSystem payroll = new PayrollSystem();

        while (true) {

            System.out.println("\n===== Employee Payroll System =====");
            System.out.println("1 Add Full Time Employee");
            System.out.println("2 Add Part Time Employee");
            System.out.println("3 Display Employees");
            System.out.println("4 Search Employee");
            System.out.println("5 Remove Employee");
            System.out.println("6 Update Salary");
            System.out.println("7 Total Payroll Expense");
            System.out.println("8 Employee Count");
            System.out.println("9 Highest Paid Employee");
            System.out.println("10 Exit");

            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1 -> {

                    sc.nextLine();

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();

                    System.out.print("Enter Salary: ");
                    double salary = sc.nextDouble();

                    System.out.print("Enter Bonus: ");
                    double bonus = sc.nextDouble();

                    payroll.addEmployee(
                            new FullTimeEmployee(name,id,salary,bonus));
                }

                case 2 -> {

                    sc.nextLine();

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();

                    System.out.print("Enter Hours Worked: ");
                    int hours = sc.nextInt();

                    System.out.print("Enter Rate Per Hour: ");
                    double rate = sc.nextDouble();

                    payroll.addEmployee(
                            new PartTimeEmployee(name,id,hours,rate));
                }

                case 3 -> payroll.displayEmployees();
                case 4 -> {
                    System.out.print("Enter ID: ");
                    payroll.searchEmployee(sc.nextInt());
                }
                case 5 -> {
                    System.out.print("Enter ID: ");
                    payroll.removeEmployee(sc.nextInt());
                }
                case 6 -> {
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    System.out.print("Enter New Salary: ");
                    double sal = sc.nextDouble();
                    payroll.updateEmployee(id,sal);
                }
                case 7 -> payroll.calculateTotalPayroll();
                case 8 -> payroll.showEmployeeCount();
                case 9 -> payroll.highestPaidEmployee();

                case 10 -> {
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                }

                default -> System.out.println("Invalid choice");
            }
        }
    }


}
