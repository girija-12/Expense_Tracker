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
    public Expense getExpenseById(int id) {
        return expenseDAO.getExpenseById(id);
    }
    public void updateExpense(Expense expense) {
        expenseDAO.updateExpense(expense);
    }
    public boolean deleteExpense(int id) {
        return expenseDAO.deleteExpense(id);
    }
}
