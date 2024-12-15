package org.example.expensetracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseDAOTest {

    private Connection connection;
    private ExpenseDAO expenseDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        // Set up in-memory H2 database
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        // Create the required tables
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE expenses (id INT AUTO_INCREMENT PRIMARY KEY, description VARCHAR(255), amount DOUBLE, category_id INT, date DATE)");
            stmt.execute("CREATE TABLE categories (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255))");
            stmt.execute("INSERT INTO categories (name) VALUES ('Food'), ('Transport'), ('Entertainment')");
        }

        expenseDAO = new ExpenseDAO(connection);
    }

    @Test
    public void testAddExpense() throws SQLException {
        // Create an expense object
        Expense expense = new Expense("Lunch", 12.5, 1, Date.valueOf("2024-12-16"));

        // Add the expense to the database
        expenseDAO.addExpense(expense);

        // Verify that the expense was added
        String sql = "SELECT * FROM expenses WHERE description = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "Lunch");
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next(), "Expense should be found in the database");
                assertEquals("Lunch", rs.getString("description"));
                assertEquals(12.5, rs.getDouble("amount"));
                assertEquals(1, rs.getInt("category_id"));
                assertEquals(Date.valueOf("2024-12-16"), rs.getDate("date"));
            }
        }
    }

    @Test
    public void testGetAllExpenses() throws SQLException {
        // Insert a few expenses
        expenseDAO.addExpense(new Expense("Lunch", 12.5, 1, Date.valueOf("2024-12-16")));
        expenseDAO.addExpense(new Expense("Bus ticket", 2.75, 2, Date.valueOf("2024-12-16")));

        // Get all expenses from the database
        List<Expense> expenses = expenseDAO.getAllExpenses();

        // Verify that there are expenses in the list
        assertNotNull(expenses, "Expenses list should not be null");
        assertEquals(2, expenses.size(), "There should be 2 expenses in the list");

        // Verify the contents of the expenses
        Expense firstExpense = expenses.get(0);
        assertEquals("Lunch", firstExpense.getDescription());
        assertEquals(12.5, firstExpense.getAmount());

        Expense secondExpense = expenses.get(1);
        assertEquals("Bus ticket", secondExpense.getDescription());
        assertEquals(2.75, secondExpense.getAmount());
    }

    @Test
    public void testAddExpense_ShouldThrowSQLException_WhenDatabaseIsDown() {
        // Simulate database connection issue by closing the connection
        try {
            connection.close();
            Expense expense = new Expense("Dinner", 20.0, 1, Date.valueOf("2024-12-16"));
            assertThrows(SQLException.class, () -> expenseDAO.addExpense(expense), "SQLException should be thrown when the database is down");
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception if needed for debugging
        } finally {
            try {
                // Reinitialize the connection after closing it
                connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
