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

public class HelloController {
    @FXML
    private TextField descriptionField, amountField, categoryField, dateField, idField;
    @FXML
    private TextArea resultArea;
    @FXML
    private ImageView backgroundImageView;

    private ExpenseDAO expenseDAO;

    public HelloController() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            this.expenseDAO = new ExpenseDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        try (InputStream imageStream = getClass().getResourceAsStream("/org/example/expensetracker/expense-tracker.jpg")) {
            if (imageStream != null) {
                Image image = new Image(imageStream);
                backgroundImageView.setImage(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addExpense() {
        try {
            String description = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String category = categoryField.getText();
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText());
            Expense expense = new Expense(description, amount, category, date);
            expenseDAO.addExpense(expense);
            resultArea.setText("Expense added successfully!");
            viewAllExpenses();
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void viewAllExpenses() {
        try {
            List<Expense> expenses = expenseDAO.getAllExpenses();
            StringBuilder builder = new StringBuilder();
            for (Expense expense : expenses) {
                builder.append(expense.toString()).append("\n");
            }
            resultArea.setText(builder.toString());
        } catch (SQLException e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void updateExpense() {
        try {
            int id = Integer.parseInt(idField.getText());
            String description = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String category = categoryField.getText();
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText());
            Expense updatedExpense = new Expense(id, description, amount, category, date);
            expenseDAO.updateExpense(id, updatedExpense);
            resultArea.setText("Expense updated successfully!");
            viewAllExpenses();
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void deleteExpenseById() {
        try {
            int id = Integer.parseInt(idField.getText());
            expenseDAO.deleteExpense(id);
            resultArea.setText("Expense with ID " + id + " deleted successfully!");
            viewAllExpenses();
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
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
        }
    }
}
