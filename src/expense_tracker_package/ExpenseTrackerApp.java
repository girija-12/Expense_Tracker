package expense_tracker_package;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.Optional;

public class ExpenseTrackerApp extends Application {

    private TableView<Expense> table;
    private ExpenseService expenseService = new ExpenseService();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Expense Tracker");

        // Create TableView and columns
        table = new TableView<>();
        table.getColumns().add(createColumn("Description", "description"));
        table.getColumns().add(createColumn("Amount", "amount"));
        table.getColumns().add(createColumn("Category", "category"));
        table.getColumns().add(createColumn("Date", "date"));

        // Load initial data
        table.getItems().addAll(expenseService.getAllExpenses());

        // Create buttons for Add, Edit, and Delete
        Button addButton = new Button("Add Expense");
        addButton.setOnAction(e -> showExpenseDialog(null, table));

        Button editButton = new Button("Edit Expense");
        editButton.setOnAction(e -> {
            Expense selectedExpense = table.getSelectionModel().getSelectedItem();
            if (selectedExpense != null) {
                showExpenseDialog(selectedExpense, table);
            }
        });

        Button deleteButton = new Button("Delete Expense");
        deleteButton.setOnAction(e -> {
            Expense selectedExpense = table.getSelectionModel().getSelectedItem();
            if (selectedExpense != null) {
                expenseService.deleteExpense(selectedExpense);
                table.getItems().remove(selectedExpense);
            }
        });

        // Layout
        BorderPane layout = new BorderPane();
        layout.setCenter(table);
        ToolBar toolBar = new ToolBar(addButton, editButton, deleteButton);
        layout.setTop(toolBar);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private <T> TableColumn<Expense, T> createColumn(String title, String property) {
        TableColumn<Expense, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));  // Using PropertyValueFactory
        return column;
    }

    private void showExpenseDialog(Expense expense, TableView<Expense> table) {
        Dialog<Expense> dialog = new Dialog<>();
        dialog.setTitle("Add / Edit Expense");

        // Create buttons
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");
        TextField dateField = new TextField();
        dateField.setPromptText("YYYY-MM-DD");

        // Pre-fill fields if editing an existing expense
        if (expense != null) {
            descriptionField.setText(expense.getDescription());
            amountField.setText(String.valueOf(expense.getAmount()));
            categoryField.setText(expense.getCategory());
            dateField.setText(expense.getDate().toString());
            }
        grid.add(new Label("Description:"), 0, 0);
        grid.add(descriptionField, 1, 0);
        grid.add(new Label("Amount:"), 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryField, 1, 2);
        grid.add(new Label("Date:"), 0, 3);
        grid.add(dateField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Convert result to Expense object when Save is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String description = descriptionField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String category = categoryField.getText();
                LocalDate date = LocalDate.parse(dateField.getText());

                if (expense == null) {
                    Expense newExpense = new Expense(description, amount, category, date);
                    expenseService.addExpense(newExpense);
                    table.getItems().add(newExpense);
                } else {
                    Expense updatedExpense = new Expense(description, amount, category, date);
                    expenseService.updateExpense(expense, updatedExpense);
                    table.getItems().set(table.getItems().indexOf(expense), updatedExpense);
                }
                return new Expense(description, amount, category, date);
            }
            return null;
        });

        Optional<Expense> result = dialog.showAndWait();
        result.ifPresent(exp -> {
            table.refresh(); // Refresh the table view to reflect changes
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
