package expense_tracker_package;

public class Expense {
    private int id;
    private String description;
    private double amount;

    public Expense() {
    }

    public Expense(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "ID: " + id + ", Description: " + description + ", Amount: " + amount;
    }
}
