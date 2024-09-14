package expense_tracker_package;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ExpenseTrackerApp extends Application {

    private ExpenseService expenseService = new ExpenseService();
    private ObservableList<Expense> expenseData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Expense Tracker");
   
        // Table for displaying expenses
        TableView<Expense> table = new TableView<>();
        TableColumn<Expense, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Expense, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        TableColumn<Expense, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());

        table.getColumns().add(idColumn);
        table.getColumns().add(descriptionColumn);
        table.getColumns().add(amountColumn);

        // Buttons for CRUD operations
        Button addButton = new Button("Add Expense");
        addButton.setOnAction(e -> showAddExpenseDialog());

        Button updateButton = new Button("Update Selected Expense");
        updateButton.setOnAction(e -> showUpdateExpenseDialog(table.getSelectionModel().getSelectedItem()));

        Button deleteButton = new Button("Delete Selected Expense");
        deleteButton.setOnAction(e -> deleteExpense(table.getSelectionModel().getSelectedItem()));

        // Load expenses
        loadExpenses();

        // Displaying expenses in the table
        table.setItems(expenseData);

        VBox vbox = new VBox(10, table, addButton, updateButton, deleteButton);
        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadExpenses() {
        List<Expense> expenses = expenseService.getAllExpenses();
        expenseData.clear();
        expenseData.addAll(expenses);
    }

    private void showAddExpenseDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Expense");
        dialog.setHeaderText("Enter Expense Details");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContentText("Description: ");
        TextField descriptionField = new TextField();
        TextField amountField = new TextField();
        dialogPane.setContent(new VBox(10, new Label("Description:"), descriptionField, new Label("Amount:"), amountField));

        dialog.showAndWait().ifPresent(result -> {
            String description = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText());
            Expense expense = new Expense(description, amount);
            expenseService.addExpense(expense);
            loadExpenses(); // Refresh table
        });
    }

    private void showUpdateExpenseDialog(Expense selectedExpense) {
        if (selectedExpense == null) {
            showErrorDialog("No Expense Selected", "Please select an expense to update.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Expense");
        dialog.setHeaderText("Update Expense Details");

        DialogPane dialogPane = dialog.getDialogPane();
        TextField descriptionField = new TextField(selectedExpense.getDescription());
        TextField amountField = new TextField(String.valueOf(selectedExpense.getAmount()));

        dialogPane.setContent(new VBox(10, new Label("Description:"), descriptionField, new Label("Amount:"), amountField));

        dialog.showAndWait().ifPresent(result -> {
            selectedExpense.setDescription(descriptionField.getText());
            selectedExpense.setAmount(Double.parseDouble(amountField.getText()));
            expenseService.updateExpense(selectedExpense);
            loadExpenses(); // Refresh table
        });
    }

    private void deleteExpense(Expense selectedExpense) {
        if (selectedExpense == null) {
            showErrorDialog("No Expense Selected", "Please select an expense to delete.");
            return;
        }

        boolean success = expenseService.deleteExpense(selectedExpense.getId());
        if (success) {
            loadExpenses();
        } else {
            showErrorDialog("Error Deleting Expense", "Unable to delete the selected expense.");
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
