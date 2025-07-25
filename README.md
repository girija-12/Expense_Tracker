# Expense Tracker Application

A JavaFX-based desktop application to help you track your expenses efficiently.  
Allows you to add, edit, delete expenses, and manage expense categories dynamically.

---

## Table of Contents

- [Overview](#overview)  
- [Features](#features)  
- [Prerequisites](#prerequisites)  
- [Setup and Installation](#setup-and-installation)  
- [Running the Application](#running-the-application)  
- [Usage](#usage)  
- [Screenshots](#screenshots)
  
---

## Overview

The Expense Tracker application is built using JavaFX for the user interface and follows a simple MVC pattern with an `ExpenseService` handling data operations.

Users can:  
- Add new expenses with description, amount, category, and date  
- Edit existing expenses  
- Delete expenses  
- Manage categories by adding, editing, and deleting categories  

The app uses an in-memory data store (via `ExpenseService` and `ExpenseDAO`), which can be extended to persistent storage if desired.

---

## Features

- User-friendly interface with a table view of all expenses  
- Editable categories with validation to avoid duplicates  
- Input validation for amounts (must be positive numbers) and dates (no future dates)  
- Date picker integration for expense dates  
- Toolbar buttons for common actions  
- Dialogs for adding/editing expenses and managing categories  
- Informative alerts for errors and confirmations  

---

## Prerequisites

Make sure you have the following installed:

- **Java Development Kit (JDK) 11 or higher**  
- **JavaFX SDK 11 or higher** (JavaFX is not bundled with newer JDKs)  
- An IDE such as **IntelliJ IDEA**, **Eclipse**, or **NetBeans** recommended  

---

## Setup and Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/girija-12/Expense_Tracker.git
   cd Expense_Tracker
   ```

2. **Set Environment Variables for Database Connection (if applicable)**

   Before running the application, make sure to set the following environment variables to configure the database connection:

   #### On Linux/macOS (bash)
   
   ```bash
   export DB_URL=database_url
   export DB_USER=root
   export DB_PASSWORD=your_password
   ```
   Replace `your_password` with your actual MySQL root password.
   
   #### On Windows (Command Prompt)
   
   ```cmd
   set DB_URL=jdbc: database_url
   set DB_USER=root
   set DB_PASSWORD=your_password
   ```
   Note: These environment variables are necessary for the application to connect to the MySQL database.

3. **Create the Database and Table**

   Before running the application, ensure the `ExpenseTracker` database and the `expenses` table exist in your MySQL server.
   
   #### SQL Script
   
   Run the following SQL commands using a MySQL client (e.g., MySQL Workbench or terminal):
   
   ```sql
   -- Create the database
   CREATE DATABASE IF NOT EXISTS ExpenseTracker;
   
   -- Use the database
   USE ExpenseTracker;
   
   -- Create the expenses table
   CREATE TABLE IF NOT EXISTS expenses (
       description VARCHAR(100),
       amount DECIMAL(10,2),
       category VARCHAR(25),
       date DATE
   );
   ```

4. **Add JavaFX libraries to your project:**

   - Download JavaFX SDK from [Gluon](https://gluonhq.com/products/javafx/).
   - Configure your IDE to add the JavaFX modules `javafx.controls` and `javafx.fxml` as library dependencies.

5. **Build the project:**

   Use your IDE’s build tools to compile the source files.

---

## Running the Application

- Run the `expense_tracker_package.ExpenseTrackerApp` class from your IDE.

---

## Running from Command Line

If running from the command line, use the following command (replace paths accordingly):

```bash
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp your-classpath expense_tracker_package.ExpenseTrackerApp
```
Replace `/path/to/javafx-sdk/lib` with the path to your JavaFX SDK lib directory.

Replace `your-classpath` with the path to your compiled classes or jar files.

---

## Usage

- **Add Expense:** Click the **Add Expense** button and fill in the form (description, amount, category).

- **Edit Expense:** Select an expense from the table and click **Edit Expense** to modify its details.

- **Delete Expense:** Select an expense and click **Delete Expense** to remove it.

- **Manage Categories:** Click **Manage Category** to add, edit, or delete expense categories.

- For new expenses, the date is fixed to today. When editing, the date can be changed (but not to a future date).

- Amount must be a positive number; categories must be unique and non-empty.

---

## Screenshots

You can find screenshots of the application in the `/screenshots` folder. These demonstrate the main UI, adding/editing expense dialogs, and category management dialogs.
