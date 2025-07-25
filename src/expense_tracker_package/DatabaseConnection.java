package expense_tracker_package;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections to the ExpenseTracker MySQL database.
 *
 * <p>This class reads the database URL, username, and password from environment variables
 * for better security and flexibility:
 * <ul>
 *   <li><strong>DB_URL</strong> - JDBC URL for the database (e.g., jdbc:mysql://localhost:3306/ExpenseTracker)</li>
 *   <li><strong>DB_USER</strong> - Database username</li>
 *   <li><strong>DB_PASSWORD</strong> - Database password</li>
 * </ul>
 *
 * <p>Make sure these environment variables are set before running the application.
 * 
 * <p>Usage example:
 * <pre>{@code
 * Connection conn = DatabaseConnection.getConnection();
 * }</pre>
 * 
 * @author
 */
public class DatabaseConnection {

    /**
     * Establishes and returns a connection to the MySQL database using credentials
     * from environment variables.
     *
     * @return a {@link Connection} object for interacting with the database
     * @throws RuntimeException if the connection fails or environment variables are not set
     */
    public static Connection getConnection() {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (url == null || user == null || password == null) {
            throw new RuntimeException("Database credentials are not properly set in environment variables.");
        }

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
}
