package expense_tracker_package;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    private static final String INSERT_QUERY = "INSERT INTO expenses (description, amount, category, date) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM expenses";
    private static final String UPDATE_QUERY = "UPDATE expenses SET description = ?, amount = ?, category = ?, date = ? WHERE description = ? AND amount = ? AND category = ? AND date = ?";
    private static final String DELETE_QUERY = "DELETE FROM expenses WHERE description = ? AND amount = ? AND category = ? AND date = ?";

    public void addExpense(Expense expense) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            setPreparedStatement(statement, expense);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding expense", e);
        }
    }
    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Expense expense = new Expense(
                        resultSet.getString("description"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("category"),
                        resultSet.getDate("date").toLocalDate()
                );
                expenses.add(expense);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving expenses", e);
        }
        return expenses;
    }
    public void updateExpense(Expense newExpense, Expense oldExpense) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            setPreparedStatement(statement, newExpense);
            setPreparedStatement(statement, oldExpense, 5);  // Parameters for the WHERE clause
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating expense", e);
        }
    }
    public boolean deleteExpense(Expense expense) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            setPreparedStatement(statement, expense);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting expense", e);
        }
    }
    private void setPreparedStatement(PreparedStatement statement, Expense expense) throws SQLException {
        setPreparedStatement(statement, expense, 1);
    }
    private void setPreparedStatement(PreparedStatement statement, Expense expense, int startIndex) throws SQLException {
        statement.setString(startIndex, expense.getDescription());
        statement.setDouble(startIndex + 1, expense.getAmount());
        statement.setString(startIndex + 2, expense.getCategory());
        statement.setDate(startIndex + 3, Date.valueOf(expense.getDate()));
    }
}
