package expense_tracker_package;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    private static final String INSERT_QUERY = "INSERT INTO expenses (description, amount, category, date) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM expenses";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM expenses WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE expenses SET description = ?, amount = ?, category = ?, date = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM expenses WHERE id = ?";

    public void addExpense(Expense expense) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            setPreparedStatement(statement, expense);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    expense.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding expense", e);
        }
    }

    public List<Expense> getAllExpenses() {
        return executeQuery(SELECT_ALL_QUERY, null);
    }

    public Expense getExpenseById(int id) {
        List<Expense> expenses = executeQuery(SELECT_BY_ID_QUERY, stmt -> stmt.setInt(1, id));
        return expenses.isEmpty() ? null : expenses.get(0);
    }

    public void updateExpense(Expense expense) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            setPreparedStatement(statement, expense);
            statement.setInt(5, expense.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating expense", e);
        }
    }

    public boolean deleteExpense(int id) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {

            statement.setInt(1, id);
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
                    expense.setId(resultSet.getInt("id"));
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
