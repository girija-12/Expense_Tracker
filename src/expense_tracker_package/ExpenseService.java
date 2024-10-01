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
    public void updateExpense(Expense expense, int index) {
        expenseDAO.updateExpense(expense, index);
    }
    public boolean deleteExpense(int id) {
        return expenseDAO.deleteExpense(id);
    }
}
