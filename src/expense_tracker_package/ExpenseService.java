package expense_tracker_package;

import java.sql.SQLException;

public class ExpenseService {

    private ExpenseDAO expenseDAO;

    public ExpenseService(ExpenseDAO expenseDAO) {
        this.expenseDAO = expenseDAO;
    }

    public void addExpense(String description, double amount, String date, String category, String paymentMethod) throws SQLException {
        // Business logic or validation can be added here before calling DAO methods
        expenseDAO.addExpense(description, amount, date, category, paymentMethod);
    }

    // Implement other service methods (Update, Delete, etc.)
}
