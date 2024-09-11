package expense_tracker_package;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpenseDAO {

    private Connection connection;

    public ExpenseDAO(Connection connection) {
        this.connection = connection;
    }

    public void addExpense(String description, double amount, String date, String category, String paymentMethod) throws SQLException {
        String insertSQL = "INSERT INTO expenses (description, amount, date, category, payment_method) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, description);
            preparedStatement.setDouble(2, amount);
            preparedStatement.setDate(3, java.sql.Date.valueOf(date));
            preparedStatement.setString(4, category.isEmpty() ? null : category);
            preparedStatement.setString(5, paymentMethod.isEmpty() ? null : paymentMethod);

            preparedStatement.executeUpdate();
        }
    }

    public ResultSet viewExpenses() throws SQLException {
        String selectSQL = "SELECT * FROM expenses";
        PreparedStatement statement = connection.prepareStatement(selectSQL);
        return statement.executeQuery();
    }

    // Implement other CRUD methods here (Update, Delete, etc.)
}
