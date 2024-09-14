package expense_tracker_package;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Expense {
    private SimpleIntegerProperty id;
    private SimpleStringProperty description;
    private SimpleDoubleProperty amount;

    public Expense() {
        this.id = new SimpleIntegerProperty();
        this.description = new SimpleStringProperty();
        this.amount = new SimpleDoubleProperty();
    }

    public Expense(String description, double amount) {
        this();
        this.description.set(description);
        this.amount.set(amount);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public SimpleDoubleProperty amountProperty() {
        return amount;
    }

    @Override
    public String toString() {
        return "ID: " + id.get() + ", Description: " + description.get() + ", Amount: " + amount.get();
    }
}
