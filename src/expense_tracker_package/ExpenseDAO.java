package expense_tracker_package;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    // Method to add an expense
    public void addExpense(Expense expense) {
        String query = "INSERT INTO expenses (description, amount, category, date) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, expense.getDescription());
            statement.setDouble(2, expense.getAmount());
            statement.setString(3, expense.getCategory());
            statement.setDate(4, Date.valueOf(expense.getDate()));  // Converts LocalDate to java.sql.Date

            statement.executeUpdate();

            // Retrieve the generated id
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                expense.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding expense", e);
        }
    }

    // Method to retrieve all expenses
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
                expense.setCategory(resultSet.getString("category"));
                
                // Handle null date
                Date sqlDate = resultSet.getDate("date");
                if (sqlDate != null) {
                    expense.setDate(sqlDate.toLocalDate());  // Converts java.sql.Date to LocalDate
                } else {
                    expense.setDate(null);  // Set null if no date
                }

                expenses.add(expense);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving expenses", e);
        }

        return expenses;
    }

    // Method to get an expense by its ID
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
                expense.setCategory(resultSet.getString("category"));
                
                Date sqlDate = resultSet.getDate("date");
                if (sqlDate != null) {
                    expense.setDate(sqlDate.toLocalDate());
                } else {
                    expense.setDate(null);
                }

                return expense;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving expense", e);
        }
        return null;
    }

    // Method to update an expense
    public void updateExpense(Expense expense) {
        String query = "UPDATE expenses SET description = ?, amount = ?, category = ?, date = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, expense.getDescription());
            statement.setDouble(2, expense.getAmount());
            statement.setString(3, expense.getCategory());
            statement.setDate(4, Date.valueOf(expense.getDate()));  // Converts LocalDate to java.sql.Date
            statement.setInt(5, expense.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating expense", e);
        }
    }

    // Method to delete an expense by its ID
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
