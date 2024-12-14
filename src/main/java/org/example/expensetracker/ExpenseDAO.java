package org.example.expensetracker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExpenseDAO {

    private final Connection connection;
    private static final Logger LOGGER = Logger.getLogger(ExpenseDAO.class.getName());

    public ExpenseDAO(Connection connection) {
        this.connection = connection;
    }

    public void addExpense(Expense expense) throws SQLException {
        String sql = "INSERT INTO expenses (description, amount, category_id, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.setInt(3, expense.getCategoryId());
            stmt.setDate(4, new java.sql.Date(expense.getDate().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding expense", e);
            throw e;
        }
    }

    public List<Expense> getAllExpenses() throws SQLException {
        String sql = "SELECT * FROM expenses";
        List<Expense> expenses = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                expenses.add(new Expense(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        rs.getInt("category_id"),
                        rs.getDate("date")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all expenses", e);
            throw e;
        }
        return expenses;
    }

    public void updateExpense(String description, Expense newExpense) throws SQLException {
        String sql = "UPDATE expenses SET description = ?, amount = ?, category_id = ?, date = ? WHERE description = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newExpense.getDescription());
            stmt.setDouble(2, newExpense.getAmount());
            stmt.setInt(3, newExpense.getCategoryId());
            stmt.setDate(4, new java.sql.Date(newExpense.getDate().getTime()));
            stmt.setString(5, description);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                LOGGER.warning("No record found to update with description: " + description);
                throw new SQLException("No record found with description: " + description);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating expense", e);
            throw e;
        }
    }

    public void deleteExpenseByDescription(String description) throws SQLException {
        String sql = "DELETE FROM expenses WHERE description = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, description);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted == 0) {
                LOGGER.warning("No record found to delete with description: " + description);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting expense", e);
            throw e;
        }
    }

    public void deleteAllExpenses() throws SQLException {
        String sql = "DELETE FROM expenses";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting all expenses", e);
            throw e;
        }
    }

    public List<Expense> getExpensesByMonth(String month) throws SQLException {
        String sql = "SELECT * FROM expenses WHERE DATE_FORMAT(date, '%Y-%m') = ?";
        List<Expense> expenses = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, month);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    expenses.add(new Expense(
                            rs.getInt("id"),
                            rs.getString("description"),
                            rs.getDouble("amount"),
                            rs.getInt("category_id"),
                            rs.getDate("date")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching expenses by month", e);
            throw e;
        }
        return expenses;
    }

    public List<Expense> getExpensesByCategory(String category) throws SQLException {
        String sql = "SELECT * FROM expenses WHERE category_id = (SELECT id FROM categories WHERE name = ?)";
        List<Expense> expenses = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    expenses.add(new Expense(
                            rs.getInt("id"),
                            rs.getString("description"),
                            rs.getDouble("amount"),
                            rs.getInt("category_id"),
                            rs.getDate("date")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching expenses by category", e);
            throw e;
        }
        return expenses;
    }

    public int getCategoryIdByName(String categoryName) throws SQLException {
        String sql = "SELECT id FROM categories WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, categoryName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    LOGGER.warning("Category not found: " + categoryName);
                    return -1;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching category ID", e);
            throw e;
        }
    }
}