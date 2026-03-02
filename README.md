Employee Payroll System
Description

The Employee Payroll System is a Java-based console application developed using Object-Oriented Programming (OOP) concepts. The system is designed to manage employee information and calculate salaries for different types of employees such as Full-Time and Part-Time employees.

This project demonstrates the use of abstraction, inheritance, polymorphism, and encapsulation to design a structured payroll management application. The system allows users to perform operations such as adding employees, displaying employee details, searching employees by ID, removing employees, updating salary information, and calculating the total payroll expense.

Employee records are stored dynamically using the ArrayList collection framework, which allows efficient management of employee data. The application also uses file handling to save employee information in a text file so that the data can be loaded again when the program starts.

Features

Add Full-Time Employees

Add Part-Time Employees

Display Employee Details

Search Employee by ID

Remove Employee

Update Employee Salary

Calculate Total Payroll Expense

Save Employee Records to File

Load Employee Records from File

Display Total Employee Count

Technologies Used

Java

Object-Oriented Programming (OOP)

Java Collections (ArrayList)

File Handling

Scanner for User Input

OOP Concepts Used

Abstraction – Implemented using the abstract Employee class

Inheritance – FullTimeEmployee and PartTimeEmployee extend the Employee class

Polymorphism – calculateSalary() method is overridden in subclasses

Encapsulation – Employee attributes are declared private and accessed through getter methods

Project Structure
Employee-Payroll-System
│
├── Main.java
├── employees.txt
└── README.md
