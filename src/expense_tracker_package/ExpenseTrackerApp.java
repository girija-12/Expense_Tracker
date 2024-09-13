package expense_tracker_package;

import java.util.Scanner;

public class ExpenseTrackerApp {
    private static ExpenseService expenseService = new ExpenseService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nExpense Tracker");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Update Expense");
            System.out.println("4. Delete Expense");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addExpense();
                    break;
                case 2:
                    viewExpenses();
                    break;
                case 3:
                    updateExpense();
                    break;
                case 4:
                    deleteExpense();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addExpense() {
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        Expense expense = new Expense(description, amount);
        expenseService.addExpense(expense);
        System.out.println("Expense added successfully!");
    }

    private static void viewExpenses() {
        System.out.println("\nExpenses:");
        for (Expense expense : expenseService.getAllExpenses()) {
            System.out.println(expense);
        }
    }

    private static void updateExpense() {
        System.out.print("Enter the ID of the expense to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Expense existingExpense = expenseService.getExpenseById(id);
        if (existingExpense == null) {
            System.out.println("Expense not found!");
            return;
        }

        System.out.print("Enter new description (leave blank to keep current): ");
        String description = scanner.nextLine();
        System.out.print("Enter new amount (enter -1 to keep current): ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (!description.isEmpty()) {
            existingExpense.setDescription(description);
        }
        if (amount != -1) {
            existingExpense.setAmount(amount);
        }

        expenseService.updateExpense(existingExpense);
        System.out.println("Expense updated successfully!");
    }

    private static void deleteExpense() {
        System.out.print("Enter the ID of the expense to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (expenseService.deleteExpense(id)) {
            System.out.println("Expense deleted successfully!");
        } else {
            System.out.println("Expense not found!");
        }
    }
}
