package org.example.expensetracker;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloController {
    @FXML
    private TextField descriptionField, amountField, categoryField, dateField, idField;
    @FXML
    private TextArea resultArea;
    @FXML
    private ImageView backgroundImageView;

    private final ExpenseDAO expenseDAO;
    private static final Logger LOGGER = Logger.getLogger(HelloController.class.getName());

    public HelloController() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            this.expenseDAO = new ExpenseDAO(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
    }

    @FXML
    public void initialize() {
        loadBackgroundImage();
    }

    private void loadBackgroundImage() {
        try (InputStream imageStream = getClass().getResourceAsStream("/org/example/expensetracker/expense-tracker.jpg")) {
            if (imageStream != null) {
                Image image = new Image(imageStream);
                backgroundImageView.setImage(image);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error loading background image", e);
        }
    }

    private int getIdFromField() throws NumberFormatException {
        return Integer.parseInt(idField.getText());
    }

    private Date getDateFromField() throws Exception {
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText());
    }

    @FXML
    public void addExpense() {
        try {
            int id = getIdFromField();
            String description = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String category = categoryField.getText();
            Date date = getDateFromField();

            Expense expense = new Expense(id, description, amount, category, date);
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
            StringBuilder builder = new StringBuilder();
            for (Expense expense : expenses) {
                builder.append(expense).append("\n");
            }
            resultArea.setText(builder.toString());
        } catch (SQLException e) {
            resultArea.setText("Error: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error fetching expenses", e);
        }
    }

    @FXML
    public void updateExpense() {
        try {
            int id = getIdFromField();
            String description = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String category = categoryField.getText();
            Date date = getDateFromField();

            Expense updatedExpense = new Expense(id, description, amount, category, date);
            expenseDAO.updateExpense(id, updatedExpense);

            resultArea.setText("Expense updated successfully!");
            viewAllExpenses();
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error updating expense", e);
        }
    }

    @FXML
    public void deleteExpenseById() {
        try {
            int id = getIdFromField();
            expenseDAO.deleteExpense(id);
            resultArea.setText("Expense with ID " + id + " deleted successfully!");
            viewAllExpenses();
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error deleting expense", e);
        }
    }

    @FXML
    public void deleteExpenses() {
        try {
            expenseDAO.deleteAllExpenses();
            resultArea.setText("All expenses deleted successfully!");
            viewAllExpenses();
        } catch (SQLException e) {
            resultArea.setText("Error: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error deleting all expenses", e);
        }
    }
}
