package org.example.expensetracker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Expense {
    private final int id;
    private final String description;
    private final double amount;
    private final String category;
    private final Date date;

    public Expense(int id, String description, double amount, String category, Date date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public Expense(String description, double amount, String category, Date date) {
        this(0, description, amount, category, date);
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "Expense{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", date=" + sdf.format(date) +
                '}';
    }
}
