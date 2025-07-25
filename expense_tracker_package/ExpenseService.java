package expense_tracker_package;

import java.util.List;

public class ExpenseService {
    private final ExpenseDAO expenseDAO = new ExpenseDAO();

    public void addExpense(Expense expense) {
        expenseDAO.addExpense(expense);
    }
    public List<Expense> getAllExpenses() {
        return expenseDAO.getAllExpenses();
    }
    public void updateExpense(Expense newExpense, Expense oldExpense) {
        expenseDAO.updateExpense(newExpense, oldExpense);
    }
    public boolean deleteExpense(Expense expense) {
        return expenseDAO.deleteExpense(expense);
    }
}
