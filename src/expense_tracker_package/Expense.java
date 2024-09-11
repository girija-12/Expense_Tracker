package expense_tracker_package;

public class Expense {

    private int id;
    private String description;
    private double amount;
    private String date;
    private String category;
    private String paymentMethod;

    public Expense(int id, String description, double amount, String date, String category, String paymentMethod) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.paymentMethod = paymentMethod;
    }

    // Getters and setters for each field
    // ...
}
