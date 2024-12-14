package org.example.expensetracker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Expense {
    private final int id;
    private final String description;
    private final double amount;
    private final int categoryId;
    private final Date date;

    public Expense(int id, String description, double amount, int categoryId, Date date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.categoryId = categoryId;
        this.date = date;
    }

    public Expense(String description, double amount, int categoryId, Date date) {
        this(0, description, amount, categoryId, date);
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public int getCategoryId() {
        return categoryId;
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
                ", categoryId=" + categoryId +
                ", date=" + sdf.format(date) +
                '}';
    }
}
