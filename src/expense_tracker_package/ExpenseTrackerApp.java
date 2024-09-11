package expense_tracker_package;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ExpenseTrackerApp extends Application {

    private ExpenseService expenseService;

    @Override
    public void start(Stage primaryStage) {
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            ExpenseDAO expenseDAO = new ExpenseDAO(dbConnection.getConnection());
            expenseService = new ExpenseService(expenseDAO);

            primaryStage.setTitle("Expense Tracker");

            // UI Components
            Label descLabel = new Label("Description:");
            TextField descField = new TextField();

            Label amountLabel = new Label("Amount:");
            TextField amountField = new TextField();

            Label dateLabel = new Label("Date (YYYY-MM-DD):");
            TextField dateField = new TextField();

            Label categoryLabel = new Label("Category:");
            TextField categoryField = new TextField();

            Label paymentLabel = new Label("Payment Method:");
            TextField paymentField = new TextField();

            Button addButton = new Button("Add Expense");
            TextArea outputArea = new TextArea();

            // GridPane Layout
            GridPane grid = new GridPane();
            grid.setPadding(new Insets(10, 10, 10, 10));
            grid.setVgap(8);
            grid.setHgap(10);

            grid.add(descLabel, 0, 0);
            grid.add(descField, 1, 0);
            grid.add(amountLabel, 0, 1);
            grid.add(amountField, 1, 1);
            grid.add(dateLabel, 0, 2);
            grid.add(dateField, 1, 2);
            grid.add(categoryLabel, 0, 3);
            grid.add(categoryField, 1, 3);
            grid.add(paymentLabel, 0, 4);
            grid.add(paymentField, 1, 4);
            grid.add(addButton, 1, 5);
            grid.add(outputArea, 0, 6, 2, 1);

            // Button Action
            addButton.setOnAction(e -> {
                try {
                    expenseService.addExpense(descField.getText(), Double.parseDouble(amountField.getText()), 
                                              dateField.getText(), categoryField.getText(), paymentField.getText());
                    outputArea.appendText("Expense added successfully!\n");
                } catch (Exception ex) {
                    showErrorDialog("Error", "Failed to add expense. " + ex.getMessage());
                }
            });

            // Set up the Scene
            Scene scene = new Scene(grid, 400, 300);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            showErrorDialog("Error", "Failed to connect to the database. " + e.getMessage());
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
