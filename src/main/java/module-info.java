module org.example.expensetracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.junit.jupiter.api;


    opens org.example.expensetracker to javafx.fxml;
    exports org.example.expensetracker;
}