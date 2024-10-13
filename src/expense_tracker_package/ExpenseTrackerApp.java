package expense_tracker_package;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.Optional;
public class ExpenseTrackerApp extends Application {

    private TableView<Expense> table;
    private ObservableList<Expense> expenseData;
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Expense Tracker");

        // Create TableView and columns
        table = new TableView<>();
        table.getColumns().add(createColumn("Description", "description"));
        table.getColumns().add(createColumn("Amount", "amount"));
        table.getColumns().add(createColumn("Category", "category"));
        TableColumn<Expense, LocalDate> dateColumn = createColumn("Date", "date");
        table.getColumns().add(dateColumn);

        expenseData = FXCollections.observableArrayList(new ExpenseService().getAllExpenses());
        table.setItems(expenseData);

        // Create buttons for Add, Edit, and Delete
        Button addButton = new Button("Add Expense");
        addButton.setOnAction(e -> showExpenseDialog(null, null));

        Button editButton = new Button("Edit Expense");
        editButton.setOnAction(e -> {
            Expense selectedExpense = table.getSelectionModel().getSelectedItem();
            if (selectedExpense != null) {
                showExpenseDialog(selectedExpense, selectedExpense);
            }
        });

        Button deleteButton = new Button("Delete Expense");
        deleteButton.setOnAction(e -> {
            Expense selectedExpense = table.getSelectionModel().getSelectedItem();
            if (selectedExpense != null) {
                try {
                    new ExpenseService().deleteExpense(selectedExpense);
                    expenseData.remove(selectedExpense);
                } catch (RuntimeException ex) {
                    showAlert("Error", ex.getMessage());
                }
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
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }
    private void showExpenseDialog(Expense oldExpense, Expense newExpense) {
        Dialog<Expense> dialog = new Dialog<>();
        dialog.setTitle(oldExpense == null ? "Add Expense" : "Edit Expense");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

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

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Choose date");

        if (oldExpense != null) {
            descriptionField.setText(oldExpense.getDescription());
            amountField.setText(String.valueOf(oldExpense.getAmount()));
            categoryField.setText(oldExpense.getCategory());
            datePicker.setValue(oldExpense.getDate());
        } else {
            datePicker.setValue(LocalDate.now()); // Set current date for new expense
        }
        grid.add(new Label("Description:"), 0, 0);
        grid.add(descriptionField, 1, 0);
        grid.add(new Label("Amount:"), 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryField, 1, 2);
        grid.add(new Label("Date:"), 0, 3);
        grid.add(datePicker, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                LocalDate selectedDate = datePicker.getValue();
                try {
                    if (selectedDate.isAfter(LocalDate.now())) {
                        throw new IllegalArgumentException("Date cannot be in the future.");
                    }

                    return new Expense(
                            descriptionField.getText(),
                            Double.parseDouble(amountField.getText()),
                            categoryField.getText(),
                            selectedDate
                    );
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Amount must be a valid number.");
                } catch (IllegalArgumentException e) {
                    showAlert("Invalid Date", e.getMessage());
                }
            }
            return null;
        });
        Optional<Expense> result = dialog.showAndWait();
        result.ifPresent(newExpenseResult -> {
            try {
                if (oldExpense == null) {
                    expenseData.add(newExpenseResult);
                    new ExpenseService().addExpense(newExpenseResult);
                } else {
                    expenseData.set(expenseData.indexOf(oldExpense), newExpenseResult);
                    new ExpenseService().updateExpense(newExpenseResult, oldExpense);
                }
            } catch (RuntimeException e) {
                showAlert("Error", e.getMessage());
            }
        });
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
