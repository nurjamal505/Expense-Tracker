package org.example.expensetracker;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloController {

    @FXML
    private TextField descriptionField, amountField, categoryField, dateField;
    @FXML
    private TextArea resultArea;

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
    public void addExpense() {
        try {
            String description = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String category = categoryField.getText();
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText());

            Expense expense = new Expense(description, amount, category, date);
            expenseDAO.addExpense(expense);

            resultArea.setText("Expense added successfully!");
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void viewAllExpenses() {
        try {
            resultArea.setText(expenseDAO.getAllExpenses().toString());
        } catch (SQLException e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }
}
