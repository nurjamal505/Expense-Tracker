module org.example.expensetracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.expensetracker to javafx.fxml;
    exports org.example.expensetracker;
}