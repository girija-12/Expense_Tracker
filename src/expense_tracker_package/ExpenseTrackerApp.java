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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;

public class ExpenseTrackerApp extends Application {

    // Table to display expenses
    private TableView<Expense> table;
    // Observable list to hold expense data
    private ObservableList<Expense> expenseData;
    // Observable list to manage categories
    private ObservableList<String> categories;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Expense Tracker");

        // Initialize categories with some default values
        categories = FXCollections.observableArrayList("Food", "Transport", "Utilities", "Entertainment");

        // Create TableView and add columns for expense attributes
        table = new TableView<>();
        table.getColumns().add(createColumn("Description", "description"));
        table.getColumns().add(createColumn("Amount", "amount"));
        table.getColumns().add(createColumn("Category", "category"));
        table.getColumns().add(createColumn("Date", "date"));

        // Load existing expenses into the table
        expenseData = FXCollections.observableArrayList(new ExpenseService().getAllExpenses());
        table.setItems(expenseData);

        // Create buttons for adding, editing, deleting expenses, and managing categories
        Button addButton = new Button("Add Expense");
        addButton.setOnAction(e -> showExpenseDialog(null));

        Button editButton = new Button("Edit Expense");
        editButton.setOnAction(e -> {
            Expense selectedExpense = table.getSelectionModel().getSelectedItem();
            if (selectedExpense != null) {
                showExpenseDialog(selectedExpense);
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

        Button changeCategoryButton = new Button("Change Category");
        changeCategoryButton.setOnAction(e -> showCategoryManagementDialog());

        // Set up the toolbar layout
        ToolBar toolBar = new ToolBar(addButton, editButton, deleteButton, changeCategoryButton);
        BorderPane layout = new BorderPane();
        layout.setCenter(table);
        layout.setTop(toolBar);

        // Create and display the main application scene
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Create a TableColumn for the specified title and property
    private <T> TableColumn<Expense, T> createColumn(String title, String property) {
        TableColumn<Expense, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    // Show a dialog for adding or editing an expense
    private void showExpenseDialog(Expense oldExpense) {
        Dialog<Expense> dialog = new Dialog<>();
        dialog.setTitle(oldExpense == null ? "Add Expense" : "Edit Expense");

        // Define buttons for the dialog
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create a grid pane for input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Input fields for expense details
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        // Dropdown for selecting categories
        ComboBox<String> categoryComboBox = new ComboBox<>(categories);
        categoryComboBox.setEditable(true); // Allow user to add new categories

        // Date picker for selecting the expense date
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Choose date");

        // Populate fields if editing an existing expense
        if (oldExpense != null) {
            descriptionField.setText(oldExpense.getDescription());
            amountField.setText(String.valueOf(oldExpense.getAmount()));
            categoryComboBox.setValue(oldExpense.getCategory());
            datePicker.setValue(oldExpense.getDate());
        } else {
            datePicker.setValue(LocalDate.now());
            datePicker.setDisable(true); // Disable date selection for new expenses
        }

        // Add input fields to the grid
        grid.add(new Label("Description:"), 0, 0);
        grid.add(descriptionField, 1, 0);
        grid.add(new Label("Amount:"), 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryComboBox, 1, 2);
        grid.add(new Label("Date:"), 0, 3);
        grid.add(datePicker, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                LocalDate selectedDate = datePicker.getValue();
                try {
                    // Validate date and amount
                    if (selectedDate.isAfter(LocalDate.now())) {
                        throw new IllegalArgumentException("Date cannot be in the future.");
                    }
                    double amount = Double.parseDouble(amountField.getText());
                    if (amount <= 0) {
                        throw new IllegalArgumentException("Amount must be positive.");
                    }

                    // Return a new Expense object
                    return new Expense(
                            descriptionField.getText(),
                            amount,
                            categoryComboBox.getValue(),
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

        // Handle the result from the dialog
        Optional<Expense> result = dialog.showAndWait();
        result.ifPresent(newExpenseResult -> {
            try {
                // Add or update the expense in the data list
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

    // Show a dialog to manage expense categories
    private void showCategoryManagementDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Manage Categories");

        // Create a VBox to hold the category list and management buttons
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        // Display current categories in a ListView
        ListView<String> categoryListView = new ListView<>(categories);
        categoryListView.setPrefHeight(150);

        // Add buttons for managing categories
        Button addCategoryButton = new Button("Add Category");
        Button editCategoryButton = new Button("Edit Category");
        Button deleteCategoryButton = new Button("Delete Category");

        // Set button actions for category management
        addCategoryButton.setOnAction(e -> showAddCategoryDialog());
        editCategoryButton.setOnAction(e -> {
            String selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
            if (selectedCategory != null) {
                showEditCategoryDialog(selectedCategory);
            } else {
                showAlert("Error", "Please select a category to edit.");
            }
        });
        deleteCategoryButton.setOnAction(e -> {
            String selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
            if (selectedCategory != null) {
                showDeleteCategoryDialog(selectedCategory);
            } else {
                showAlert("Error", "Please select a category to delete.");
            }
        });

        // Add components to the VBox
        vbox.getChildren().addAll(new Label("Current Categories:"), categoryListView, addCategoryButton, editCategoryButton, deleteCategoryButton);

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        dialog.showAndWait();
    }

    // Show dialog to add a new category
    private void showAddCategoryDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Category");
        dialog.setHeaderText("Enter new category name:");
        dialog.setContentText("Category name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newCategory -> {
            // Ensure the category is unique and not empty
            if (!newCategory.isEmpty() && !categories.contains(newCategory)) {
                categories.add(newCategory);
                showAlert("Success", "Category added successfully!");
            } else {
                showAlert("Error", "Category name must be unique and not empty.");
            }
        });
    }

    // Show dialog to edit an existing category
    private void showEditCategoryDialog(String oldCategory) {
        TextInputDialog dialog = new TextInputDialog(oldCategory);
        dialog.setTitle("Edit Category");
        dialog.setHeaderText("Edit category name:");
        dialog.setContentText("New category name:");

        Optional<String> newCategoryResult = dialog.showAndWait();
        newCategoryResult.ifPresent(newCategory -> {
            // Ensure the new category is unique and not empty
            if (!newCategory.isEmpty() && !categories.contains(newCategory)) {
                categories.remove(oldCategory);
                categories.add(newCategory);
                showAlert("Success", "Category edited successfully!");
            } else {
                showAlert("Error", "Category name must be unique and not empty.");
            }
        });
    }

    // Show dialog to delete a category
    private void showDeleteCategoryDialog(String category) {
        categories.remove(category);
        showAlert("Success", "Category deleted successfully!");
    }

    // Utility method to show an alert dialog
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}
