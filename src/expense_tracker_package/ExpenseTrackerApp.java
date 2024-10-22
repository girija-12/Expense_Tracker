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
    private ObservableList<String> categories;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Expense Tracker");

        // Initialize categories
        categories = FXCollections.observableArrayList("Food", "Transport", "Utilities", "Entertainment");

        // Create TableView and columns
        table = new TableView<>();
        table.getColumns().add(createColumn("Description", "description"));
        table.getColumns().add(createColumn("Amount", "amount"));
        table.getColumns().add(createColumn("Category", "category"));
        table.getColumns().add(createColumn("Date", "date"));

        expenseData = FXCollections.observableArrayList(new ExpenseService().getAllExpenses());
        table.setItems(expenseData);

        // Create buttons for Add, Edit, Delete, and Change Category
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

        // Layout
        ToolBar toolBar = new ToolBar(addButton, editButton, deleteButton, changeCategoryButton);
        BorderPane layout = new BorderPane();
        layout.setCenter(table);
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

    private void showExpenseDialog(Expense oldExpense) {
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

        // Dropdown for categories
        ComboBox<String> categoryComboBox = new ComboBox<>(categories);
        categoryComboBox.setEditable(true); // Allow typing new categories

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Choose date");

        if (oldExpense != null) {
            descriptionField.setText(oldExpense.getDescription());
            amountField.setText(String.valueOf(oldExpense.getAmount()));
            categoryComboBox.setValue(oldExpense.getCategory());
            datePicker.setValue(oldExpense.getDate());
        } else {
            datePicker.setValue(LocalDate.now());
        }

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
                    if (selectedDate.isAfter(LocalDate.now())) {
                        throw new IllegalArgumentException("Date cannot be in the future.");
                    }
                    double amount = Double.parseDouble(amountField.getText());
                    if (amount <= 0) {
                        throw new IllegalArgumentException("Amount must be positive.");
                    }

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

    private void showCategoryManagementDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Manage Categories");

        ButtonType addCategoryButton = new ButtonType("Add Category");
        ButtonType editCategoryButton = new ButtonType("Edit Category");
        ButtonType deleteCategoryButton = new ButtonType("Delete Category");

        dialog.getDialogPane().getButtonTypes().addAll(addCategoryButton, editCategoryButton, deleteCategoryButton, ButtonType.CANCEL);

        // Show current categories
        StringBuilder currentCategories = new StringBuilder("Current Categories:\n");
        for (String category : categories) {
            currentCategories.append(category).append("\n");
        }

        Alert categoryListAlert = new Alert(Alert.AlertType.INFORMATION, currentCategories.toString());
        categoryListAlert.setTitle("Categories");
        categoryListAlert.setHeaderText(null);
        categoryListAlert.showAndWait();

        Optional<ButtonType> result = dialog.showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType == addCategoryButton) {
                showAddCategoryDialog();
            } else if (buttonType == editCategoryButton) {
                showEditCategoryDialog();
            } else if (buttonType == deleteCategoryButton) {
                showDeleteCategoryDialog();
            }
        });
    }

    private void showAddCategoryDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Category");
        dialog.setHeaderText("Enter new category name:");
        dialog.setContentText("Category name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newCategory -> {
            if (!newCategory.isEmpty() && !categories.contains(newCategory)) {
                categories.add(newCategory);
                showAlert("Success", "Category added successfully!");
            } else {
                showAlert("Error", "Category name must be unique and not empty.");
            }
        });
    }

    private void showEditCategoryDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Edit Category");
        dialog.setHeaderText("Select a category to edit:");
        dialog.setContentText("Category name:");

        String[] categoryArray = categories.toArray(new String[0]);
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(categoryArray[0], categoryArray);
        choiceDialog.setTitle("Edit Category");
        choiceDialog.setHeaderText("Choose a category to edit:");

        Optional<String> selectedCategory = choiceDialog.showAndWait();
        selectedCategory.ifPresent(oldCategory -> {
            dialog.setContentText("New category name:");
            Optional<String> newCategoryResult = dialog.showAndWait();
            newCategoryResult.ifPresent(newCategory -> {
                if (!newCategory.isEmpty() && !categories.contains(newCategory)) {
                    categories.remove(oldCategory);
                    categories.add(newCategory);
                    showAlert("Success", "Category edited successfully!");
                } else {
                    showAlert("Error", "Category name must be unique and not empty.");
                }
            });
        });
    }

    private void showDeleteCategoryDialog() {
        String[] categoryArray = categories.toArray(new String[0]);
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(categoryArray[0], categoryArray);
        choiceDialog.setTitle("Delete Category");
        choiceDialog.setHeaderText("Choose a category to delete:");

        Optional<String> selectedCategory = choiceDialog.showAndWait();
        selectedCategory.ifPresent(category -> {
            categories.remove(category);
            showAlert("Success", "Category deleted successfully!");
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
