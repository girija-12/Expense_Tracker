package expense_tracker_package;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    public void addExpense(Expense expense) {
        String query = "INSERT INTO expenses (description, amount) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, expense.getDescription());
            statement.setDouble(2, expense.getAmount());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                expense.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding expense", e);
        }
    }

    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT * FROM expenses";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Expense expense = new Expense();
                expense.setId(resultSet.getInt("id"));
                expense.setDescription(resultSet.getString("description"));
                expense.setAmount(resultSet.getDouble("amount"));
                expenses.add(expense);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving expenses", e);
        }

        return expenses;
    }

    public Expense getExpenseById(int id) {
        String query = "SELECT * FROM expenses WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Expense expense = new Expense();
                expense.setId(resultSet.getInt("id"));
                expense.setDescription(resultSet.getString("description"));
                expense.setAmount(resultSet.getDouble("amount"));
                return expense;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving expense", e);
        }
        return null;
    }

    public void updateExpense(Expense expense) {
        String query = "UPDATE expenses SET description = ?, amount = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, expense.getDescription());
            statement.setDouble(2, expense.getAmount());
            statement.setInt(3, expense.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating expense", e);
        }
    }

    public boolean deleteExpense(int id) {
        String query = "DELETE FROM expenses WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting expense", e);
        }
    }
}
