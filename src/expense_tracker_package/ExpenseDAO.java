package expense_tracker_package;

import java.sql.*;
import java.time.LocalDate;
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
        return executeQuery(SELECT_ALL_QUERY, null);
    }

    public void updateExpense(Expense oldExpense, Expense newExpense) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            setPreparedStatement(statement, newExpense);
            statement.setString(5, oldExpense.getDescription());
            statement.setDouble(6, oldExpense.getAmount());
            statement.setString(7, oldExpense.getCategory());
            statement.setDate(8, Date.valueOf(oldExpense.getDate()));
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
        statement.setString(1, expense.getDescription());
        statement.setDouble(2, expense.getAmount());
        statement.setString(3, expense.getCategory());
        statement.setDate(4, Date.valueOf(expense.getDate()));
    }

    private List<Expense> executeQuery(String query, SQLConsumer<PreparedStatement> paramSetter) {
        List<Expense> expenses = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            if (paramSetter != null) paramSetter.accept(statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Expense expense = new Expense(
                        resultSet.getString("description"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("category"),
                        resultSet.getDate("date").toLocalDate()
                    );
                    expenses.add(expense);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query", e);
        }
        return expenses;
    }

    @FunctionalInterface
    private interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }
}
