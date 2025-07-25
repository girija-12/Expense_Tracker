package expense_tracker_package;

import java.util.List;

/**
 * Service layer class that acts as an intermediary between
 * the application and the data access layer ({@link ExpenseDAO}).
 * <p>
 * Provides methods to add, retrieve, update, and delete expenses.
 * This layer can be extended later to include business logic,
 * validations, or transaction management.
 * </p>
 * 
 * @see ExpenseDAO
 * @see Expense
 * 
 */
public class ExpenseService {
    private final ExpenseDAO expenseDAO = new ExpenseDAO();

    /**
     * Adds a new expense by delegating to {@link ExpenseDAO#addExpense(Expense)}.
     *
     * @param expense the expense to add
     */
    public void addExpense(Expense expense) {
        expenseDAO.addExpense(expense);
    }

    /**
     * Retrieves all expenses by delegating to {@link ExpenseDAO#getAllExpenses()}.
     *
     * @return a list of all expenses
     */
    public List<Expense> getAllExpenses() {
        return expenseDAO.getAllExpenses();
    }

    /**
     * Updates an existing expense by delegating to {@link ExpenseDAO#updateExpense(Expense, Expense)}.
     *
     * @param newExpense the updated expense data
     * @param oldExpense the existing expense data to identify the record
     */
    public void updateExpense(Expense newExpense, Expense oldExpense) {
        expenseDAO.updateExpense(newExpense, oldExpense);
    }

    /**
     * Deletes an expense by delegating to {@link ExpenseDAO#deleteExpense(Expense)}.
     *
     * @param expense the expense to delete
     * @return true if the expense was deleted successfully, false otherwise
     */
    public boolean deleteExpense(Expense expense) {
        return expenseDAO.deleteExpense(expense);
    }
}
