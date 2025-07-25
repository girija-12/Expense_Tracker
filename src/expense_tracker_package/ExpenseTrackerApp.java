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

/**
 * JavaFX application class for the Expense Tracker UI.
 * 
 * <p>This application allows users to manage their expenses by adding,
 * editing, deleting entries, and managing expense categories.</p>
 * 
 * <p>The UI consists of a table displaying expenses and toolbar buttons
 * for interacting with the data.</p>
 * 
 * <p>Uses {@link ExpenseService} for CRUD operations on expenses.</p>
 * 
 * @see ExpenseService
 * @see Expense
 */
public class ExpenseTrackerApp extends Application {

    /** TableView displaying all expenses */
    private TableView<Expense> table;

    /** ObservableList holding expense data displayed in the table */
    private ObservableList<Expense> expenseData;

    /** ObservableList holding categories for expenses */
    private ObservableList<String> categories;

    /**
     * JavaFX application entry point. Sets up the primary stage and UI components.
     *
     * @param primaryStage the main application window
     */
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

        // Create buttons for expense operations
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

        Button manageCategoryButton = new Button("Manage Category");
        manageCategoryButton.setOnAction(e -> showCategoryManagementDialog());

        // Set up the toolbar layout with the buttons
        ToolBar toolBar = new ToolBar(addButton, editButton, deleteButton, manageCategoryButton);
        BorderPane layout = new BorderPane();
        layout.setCenter(table);
        layout.setTop(toolBar);

        // Create and display the main application scene
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates a TableColumn for the expense table.
     *
     * @param <T>      the type of data contained in the column
     * @param title    the column header title
     * @param property the property name of Expense class mapped to this column
     * @return a configured TableColumn
     */
    private <T> TableColumn<Expense, T> createColumn(String title, String property) {
        TableColumn<Expense, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    /**
     * Displays a dialog to add a new expense or edit an existing one.
     *
     * @param oldExpense the expense to edit, or null to add a new expense
     */
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

        // Convert dialog result into an Expense object when Save is clicked
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
                // Add or update the expense in the data list and database
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

    /**
     * Displays a dialog to manage the expense categories.
     * Users can add, edit, or delete categories.
     */
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

    /**
     * Shows a dialog to add a new expense category.
     */
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

    /**
     * Shows a dialog to edit an existing category name.
     *
     * @param oldCategory the existing category name to edit
     */
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

    /**
     * Deletes a category from the list.
     *
     * @param category the category name to delete
     */
    private void showDeleteCategoryDialog(String category) {
        categories.remove(category);
        showAlert("Success", "Category deleted successfully!");
    }

    /**
     * Utility method to show an information alert dialog.
     *
     * @param title   the title of the alert window
     * @param content the message content of the alert
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Main method launching the JavaFX application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
