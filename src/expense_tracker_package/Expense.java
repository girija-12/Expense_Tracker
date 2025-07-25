package expense_tracker_package;

import java.time.LocalDate;

/**
 * Represents a single expense entry in the expense tracker system.
 * <p>
 * Each expense consists of a description, amount, category, and date.
 * This class serves as a model used across the application.
 * </p>
 *
 * <p>Example:
 * <pre>{@code
 * Expense expense = new Expense("Groceries", 50.0, "Food", LocalDate.now());
 * }</pre>
 * </p>
 * 
 * @author 
 */
public class Expense {

    private String description;
    private double amount;
    private String category;
    private LocalDate date;

    /**
     * Constructs a new {@code Expense} with the given details.
     *
     * @param description A brief note or label for the expense (e.g., "Bus ticket")
     * @param amount The amount of money spent
     * @param category The category of the expense (e.g., "Transport", "Food")
     * @param date The date on which the expense occurred
     */
    public Expense(String description, double amount, String category, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    /**
     * Returns the description of the expense.
     *
     * @return a short description or note
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the expense.
     *
     * @param description a short description or note
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the amount of the expense.
     *
     * @return the amount spent
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the expense.
     *
     * @param amount the amount spent
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Returns the category of the expense.
     *
     * @return the expense category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the expense.
     *
     * @param category the expense category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns the date of the expense.
     *
     * @return the date the expense occurred
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the expense.
     *
     * @param date the date the expense occurred
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
