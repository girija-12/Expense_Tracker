package expense_tracker_package;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/ExpenseTracker";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "*MySQL01Girija";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        
    }
    
}
