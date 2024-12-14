package org.example.expensetracker;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloController {

    @FXML
    private TextField descriptionField, amountField, dateField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextArea resultArea;
    @FXML
    private ImageView backgroundImageView;

    private final ExpenseDAO expenseDAO;
    private static final Logger LOGGER = Logger.getLogger(HelloController.class.getName());

    public HelloController() {
        try {
            this.expenseDAO = new ExpenseDAO(DatabaseConnection.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
    }

    @FXML
    public void initialize() {
        loadBackgroundImage();
        loadCategories();
    }

    private void loadBackgroundImage() {
        try (InputStream imageStream = getClass().getResourceAsStream("/org/example/expensetracker/expense-tracker.jpg")) {
            if (imageStream != null) {
                Image image = new Image(imageStream);
                backgroundImageView.setImage(image);
            } else {
                LOGGER.warning("Background image not found.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error loading background image", e);
        }
    }

    private void loadCategories() {
        categoryComboBox.getItems().addAll("Food", "Transport", "Entertainment", "Utilities", "Health", "Education", "Others");
    }

    @FXML
    public void addExpense() {
        try {
            String description = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String category = categoryComboBox.getValue();
            String dateInput = dateField.getText();

            int categoryId = expenseDAO.getCategoryIdByName(category);
            if (categoryId == -1) {
                resultArea.setText("Error: Category not found");
                return;
            }

            Expense expense = new Expense(description, amount, categoryId, java.sql.Date.valueOf(dateInput));
            expenseDAO.addExpense(expense);

            resultArea.setText("Expense added successfully!");
            viewAllExpenses();
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error adding expense", e);
        }
    }

    @FXML
    public void viewAllExpenses() {
        try {
            List<Expense> expenses = expenseDAO.getAllExpenses();
            displayExpenses(expenses);
        } catch (SQLException e) {
            resultArea.setText("Error: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error fetching expenses", e);
        }
    }

    @FXML
    public void updateExpense() {
        try {
            String description = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String category = categoryComboBox.getValue();
            String dateInput = dateField.getText();

            int categoryId = expenseDAO.getCategoryIdByName(category);
            if (categoryId == -1) {
                resultArea.setText("Error: Category not found");
                return;
            }

            Expense updatedExpense = new Expense(description, amount, categoryId, java.sql.Date.valueOf(dateInput));
            expenseDAO.updateExpense(description, updatedExpense);

            resultArea.setText("Expense updated successfully!");
            viewAllExpenses();
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error updating expense", e);
        }
    }

    @FXML
    public void deleteExpenseByDescription() {
        try {
            String description = descriptionField.getText();
            expenseDAO.deleteExpenseByDescription(description);

            resultArea.setText("Expense deleted successfully!");
            viewAllExpenses();
        } catch (SQLException e) {
            resultArea.setText("Error: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error deleting expense by description", e);
        }
    }

    @FXML
    public void deleteExpenses() {
        try {
            expenseDAO.deleteAllExpenses();
            resultArea.setText("All expenses have been deleted successfully.");
            viewAllExpenses();
        } catch (SQLException e) {
            resultArea.setText("Error: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error deleting all expenses", e);
        }
    }

    @FXML
    public void filterByMonth() {
        try {
            String month = dateField.getText(); // Input format: "YYYY-MM"
            if (month == null || month.isEmpty() || !month.matches("\\d{4}-\\d{2}")) {
                resultArea.setText("Please enter a valid month in the format 'YYYY-MM'.");
                return;
            }

            List<Expense> expenses = expenseDAO.getExpensesByMonth(month);
            if (expenses.isEmpty()) {
                resultArea.setText("No expenses found for the specified month: " + month);
            } else {
                displayExpenses(expenses);
            }
        } catch (SQLException e) {
            resultArea.setText("Error: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error filtering expenses by month", e);
        }
    }

    @FXML
    public void filterByCategory() {
        try {
            String category = categoryComboBox.getValue();
            List<Expense> expenses = expenseDAO.getExpensesByCategory(category);
            displayExpenses(expenses);
        } catch (SQLException e) {
            resultArea.setText("Error: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error filtering expenses by category", e);
        }
    }

    private void displayExpenses(List<Expense> expenses) {
        StringBuilder builder = new StringBuilder();
        for (Expense expense : expenses) {
            builder.append(expense).append("\n");
        }
        resultArea.setText(builder.toString());
    }
}
