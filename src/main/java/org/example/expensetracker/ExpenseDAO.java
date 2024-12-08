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

    public void updateExpense(int id, Expense newExpense) throws SQLException {
        String sql = "UPDATE expenses SET description = ?, amount = ?, category = ?, date = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newExpense.getDescription());
            stmt.setDouble(2, newExpense.getAmount());
            stmt.setString(3, newExpense.getCategory());
            stmt.setDate(4, new java.sql.Date(newExpense.getDate().getTime()));
            stmt.setInt(5, id);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No record found with ID: " + id);
            }
        }
    }

    public void deleteExpense(int id) throws SQLException {
        String sql = "DELETE FROM expenses WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void deleteAllExpenses() throws SQLException {
        String sql = "DELETE FROM expenses";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
