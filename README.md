# Employee Payroll Management System

A Java-based console application that manages employee payroll using **Object-Oriented Programming (OOP)** concepts and **MySQL database integration via JDBC**.

This system allows users to add, update, search, delete, and manage employee records while calculating payroll expenses efficiently.

---

## Features

* Add Full-Time Employees
* Add Part-Time Employees
* Display All Employees
* Search Employee by ID
* Update Employee Salary
* Remove Employee
* Calculate Total Payroll Expense
* Display Total Employee Count
* Find Highest Paid Employee

---

## Technologies Used

* **Java**
* **JDBC (Java Database Connectivity)**
* **MySQL**
* **Object-Oriented Programming**
* **Prepared Statements**
* **Console-based UI**

---

## OOP Concepts Implemented

* **Abstraction** → `Employee` abstract class
* **Inheritance** → `FullTimeEmployee`, `PartTimeEmployee`
* **Polymorphism** → `calculateSalary()` method
* **Encapsulation** → Private fields with getters

---

## Project Structure

```
Employee-Payroll-System
│
├── src
│   └── Main.java
│
├── README.md
└── .gitignore
```


---

## Database Setup

Create the database and table using the following SQL commands:

```sql
CREATE DATABASE payroll_db;

USE payroll_db;

CREATE TABLE employees(
id INT PRIMARY KEY,
name VARCHAR(100),
type VARCHAR(20),
salary DOUBLE,
bonus DOUBLE,
hoursWorked INT,
ratePerHour DOUBLE
);
```

---

## How to Run the Project

1. Clone the repository

```
git clone https://github.com/yourusername/EmployeePayrollSystem.git
```

2. Open the project in **IntelliJ IDEA / Eclipse**

3. Add MySQL JDBC Driver

```
mysql-connector-j
```

4. Update database credentials in `DBConnection`:

```java
static final String USER = "root";
static final String PASSWORD = "your_password";
```

5. Run `Main.java`

---

## Sample Menu

```
===== Employee Payroll System =====
1 Add Full Time Employee
2 Add Part Time Employee
3 Display Employees
4 Search Employee
5 Remove Employee
6 Update Salary
7 Total Payroll Expense
8 Employee Count
9 Highest Paid Employee
10 Exit
```

---

## Learning Outcomes

This project demonstrates:

* Java OOP design
* Database integration using JDBC
* SQL CRUD operations
* Exception handling
* Modular application design

---

## Future Improvements

* GUI using **JavaFX / Swing**
* Employee authentication system
* Export payroll reports
* REST API integration
* Web-based version using **Spring Boot**

---

## Author

Developed by **Kalangi Geetaneha**

Computer Science Student  
