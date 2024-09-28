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
import java.util.Comparator;
import java.util.Optional;

public class ExpenseTrackerApp extends Application {

    private TableView<Expense> table;
    private ObservableList<Expense> expenseData;
    private boolean isSortedAscending = false;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Expense Tracker");

        // Create TableView and columns
        table = new TableView<>();
        table.getColumns().add(createColumn("Description", "description"));
        table.getColumns().add(createColumn("Amount", "amount"));
        table.getColumns().add(createColumn("Category", "category"));
        TableColumn<Expense, LocalDate> dateColumn = createColumn("Date", "date");
        table.getColumns().add(dateColumn);

        // Sort feature for Date column
        dateColumn.setSortable(false);
        Button sortButton = new Button("↑↓");
        sortButton.setStyle("-fx-text-fill: black;");
        sortButton.setOnAction(e -> {
            if (!isSortedAscending) {
                expenseData.sort(Comparator.comparing(Expense::getDate));
                sortButton.setStyle("-fx-text-fill: blue;");
            } else {
                expenseData.sort(Comparator.comparing(Expense::getDate).reversed());
                sortButton.setStyle("-fx-text-fill: black;");
            }
            isSortedAscending = !isSortedAscending;
        });

        dateColumn.setGraphic(sortButton);

        expenseData = FXCollections.observableArrayList(new ExpenseService().getAllExpenses());
        table.setItems(expenseData);

        // Create buttons for Add, Edit, and Delete
        Button addButton = new Button("Add Expense");
        addButton.setOnAction(e -> showExpenseDialog(null, null));

        Button editButton = new Button("Edit Expense");
        editButton.setOnAction(e -> {
            Expense selectedExpense = table.getSelectionModel().getSelectedItem();
            if (selectedExpense != null) {
                showExpenseDialog(selectedExpense, getExpenseIndex(selectedExpense));
            }
        });

        Button deleteButton = new Button("Delete Expense");
        deleteButton.setOnAction(e -> {
            Expense selectedExpense = table.getSelectionModel().getSelectedItem();
            if (selectedExpense != null) {
                expenseData.remove(selectedExpense);
                // Here you should also delete it from the database
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

    private void showExpenseDialog(Expense expense, Integer id) {
        Dialog<Expense> dialog = new Dialog<>();
        dialog.setTitle(expense == null ? "Add Expense" : "Edit Expense");

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

        // Date picker for selecting the date from the calendar
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Choose date");

        if (expense != null) {
            descriptionField.setText(expense.getDescription());
            amountField.setText(String.valueOf(expense.getAmount()));
            categoryField.setText(expense.getCategory());
            datePicker.setValue(expense.getDate());
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
                return new Expense(
                        descriptionField.getText(),
                        Double.parseDouble(amountField.getText()),
                        categoryField.getText(),
                        datePicker.getValue()
                );
            }
            return null;
        });

        Optional<Expense> result = dialog.showAndWait();
        result.ifPresent(expenseResult -> {
            if (expense == null) {
                expenseData.add(expenseResult);
                new ExpenseService().addExpense(expenseResult);
            } else {
                expenseData.set(id, expenseResult);
                new ExpenseService().updateExpense(expenseResult, id);
            }
        });
    }
    private Integer getExpenseIndex(Expense expense) {
        return expenseData.indexOf(expense);  // assuming this is a unique entry
    }
    public static void main(String[] args) {
        launch(args);
    }
}
