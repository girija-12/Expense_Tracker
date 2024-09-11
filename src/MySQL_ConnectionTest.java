import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL_ConnectionTest {
    public static void main(String[] args) {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/"; // Use the default port
        String user = "root"; // Replace with your MySQL username
        String password = "*MySQL01Girija"; // Replace with your MySQL password

        Connection conn = null;
        Statement stmt = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to the MySQL server
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to MySQL server was successful!");

            // Create a statement object to execute SQL queries
            stmt = conn.createStatement();

            // Create a sample database named "TestDB"
            String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS ExpenseTracker";
            stmt.executeUpdate(createDatabaseSQL);
            System.out.println("Database 'ExpenseTracker' created successfully or already exists.");

            // Use the newly created database
            stmt.executeUpdate("USE ExpenseTracker");

            // Create a sample table named "SampleTable" within "TestDB"
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Expense (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "description VARCHAR(255) NOT NULL, " +
                    "amount DECIMAL(10, 2) NOT NULL, " +
                    "date DATE NOT NULL, " +
                    "category VARCHAR(100), " +  // Optional field
                    "payment_method VARCHAR(50)" +  // Optional field
                    ");";
            stmt.executeUpdate(createTableSQL);
            System.out.println("Table 'Expense' created successfully or already exists.");

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Failed to connect to MySQL server or execute SQL query.");
            e.printStackTrace();
        } finally {
            // Close the statement and connection objects
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
