package expense_tracker_package;

import java.time.LocalDate;

public class Expense {

    private String description;
    private double amount;
    private String category;
    private LocalDate date;

    // Constructor with LocalDate
    public Expense(String description, double amount, String category, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }
    // Constructor with java.sql.Date (for backward compatibility)
    public Expense(String description, double amount, String category, java.sql.Date date) {
        this(description, amount, category, date.toLocalDate());
    }
    // Getters and setters
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
