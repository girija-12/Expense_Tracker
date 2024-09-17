package expense_tracker_package;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDate;

public class Expense {
    private SimpleIntegerProperty id;
    private SimpleStringProperty description;
    private SimpleDoubleProperty amount;
    private SimpleStringProperty category;
    private SimpleObjectProperty<LocalDate> date;

    // Constructor
    public Expense() {
        this.id = new SimpleIntegerProperty();
        this.description = new SimpleStringProperty();
        this.amount = new SimpleDoubleProperty();
        this.category = new SimpleStringProperty();
        this.date = new SimpleObjectProperty<>();
    }

    // Overloaded constructor
    public Expense(String description, double amount, String category, LocalDate date) {
        this();
        this.description.set(description);
        this.amount.set(amount);
        this.category.set(category);
        this.date.set(date);
    }

    // Getters and setters for id
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    // Getters and setters for description
    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    // Getters and setters for amount
    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public SimpleDoubleProperty amountProperty() {
        return amount;
    }

    // Getters and setters for category
    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    // Getters and setters for date
    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    @Override
    public String toString() {
        return "ID: " + id.get() + ", Description: " + description.get() + ", Amount: " + amount.get()
                + ", Category: " + category.get() + ", Date: " + date.get();
    }
}
