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
- [Project Structure](#project-structure)  
- [License](#license)  

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

2. **Add JavaFX libraries to your project:**

   - Download JavaFX SDK from [Gluon](https://gluonhq.com/products/javafx/).
   - Configure your IDE to add the JavaFX modules `javafx.controls` and `javafx.fxml` as library dependencies.

3. **Build the project:**

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
