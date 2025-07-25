package expense_tracker_package;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for performing CRUD operations on the {@link Expense} table.
 * <p>
 * This class provides methods to add, retrieve, update, and delete expense records
 * from a relational database using JDBC.
 * </p>
 * 
 * <p>It depends on {@link DatabaseConnection} for establishing the database connection.
 * Ensure that the database and `expenses` table are properly set up.</p>
 * 
 * Table Schema:
 * <pre>
 * CREATE TABLE expenses (
 *   description VARCHAR(255),
 *   amount DOUBLE,
 *   category VARCHAR(100),
 *   date DATE
 * );
 * </pre>
 * 
 * @see Expense
 * @see DatabaseConnection
 * @author 
 */
public class ExpenseDAO {

    // SQL queries for CRUD operations
    private static final String INSERT_QUERY =
            "INSERT INTO expenses (description, amount, category, date) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_QUERY =
            "SELECT * FROM expenses";
    private static final String UPDATE_QUERY =
            "UPDATE expenses SET description = ?, amount = ?, category = ?, date = ? " +
            "WHERE description = ? AND amount = ? AND category = ? AND date = ?";
    private static final String DELETE_QUERY =
            "DELETE FROM expenses WHERE description = ? AND amount = ? AND category = ? AND date = ?";

    /**
     * Inserts a new expense into the database.
     *
     * @param expense the {@link Expense} object to add
     */
    public void addExpense(Expense expense) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {

            setPreparedStatement(statement, expense);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error adding expense", e);
        }
    }

    /**
     * Retrieves all expenses from the database.
     *
     * @return a list of all {@link Expense} records
     */
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

    /**
     * Updates an existing expense in the database.
     *
     * @param newExpense the updated expense values
     * @param oldExpense the original expense used to locate the row in the database
     */
    public void updateExpense(Expense newExpense, Expense oldExpense) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            // Set new values
            setPreparedStatement(statement, newExpense);
            // Set condition for WHERE clause using the old expense
            setPreparedStatement(statement, oldExpense, 5);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating expense", e);
        }
    }

    /**
     * Deletes an expense from the database.
     *
     * @param expense the {@link Expense} object to delete
     * @return true if the expense was deleted; false otherwise
     */
    public boolean deleteExpense(Expense expense) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {

            setPreparedStatement(statement, expense);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting expense", e);
        }
    }

    /**
     * Helper method to set parameters on a {@link PreparedStatement} for a given {@link Expense}.
     *
     * @param statement the prepared statement
     * @param expense the expense to use for setting parameters
     * @throws SQLException if setting parameters fails
     */
    private void setPreparedStatement(PreparedStatement statement, Expense expense) throws SQLException {
        setPreparedStatement(statement, expense, 1);
    }

    /**
     * Helper method to set parameters on a {@link PreparedStatement} for a given {@link Expense},
     * starting at a specified index.
     *
     * @param statement the prepared statement
     * @param expense the expense to use for setting parameters
     * @param startIndex the index of the first parameter
     * @throws SQLException if setting parameters fails
     */
    private void setPreparedStatement(PreparedStatement statement, Expense expense, int startIndex) throws SQLException {
        statement.setString(startIndex, expense.getDescription());
        statement.setDouble(startIndex + 1, expense.getAmount());
        statement.setString(startIndex + 2, expense.getCategory());
        statement.setDate(startIndex + 3, Date.valueOf(expense.getDate()));
    }
}
