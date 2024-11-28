package org.example.expensetracker;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    private final Connection connection;

    public ExpenseDAO(Connection connection) {
        this.connection = connection;
    }

    public void addExpense(Expense expense) throws SQLException {
        String sql = "INSERT INTO expenses (description, amount, category, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.setString(3, expense.getCategory());
            stmt.setDate(4, new java.sql.Date(expense.getDate().getTime()));
            stmt.executeUpdate();
        }
    }

    public List<Expense> getAllExpenses() throws SQLException {
        String sql = "SELECT * FROM expenses";
        List<Expense> expenses = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                expenses.add(new Expense(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        rs.getString("category"),
                        rs.getDate("date")
                ));
            }
        }
        return expenses;
    }

    public List<Expense> getExpensesByCategory(String category) throws SQLException {
        String sql = "SELECT * FROM expenses WHERE category = ?";
        List<Expense> expenses = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    expenses.add(new Expense(
                            rs.getInt("id"),
                            rs.getString("description"),
                            rs.getDouble("amount"),
                            rs.getString("category"),
                            rs.getDate("date")
                    ));
                }
            }
        }
        return expenses;
    }

    public double calculateTotalExpenses() throws SQLException {
        String sql = "SELECT SUM(amount) AS total FROM expenses";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0.0;
    }
}
