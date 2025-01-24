module hr.java.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;

    exports hr.java.restaurant.main;
    opens hr.java.restaurant.main to javafx.fxml;

    exports hr.java.restaurant.controller;
    opens hr.java.restaurant.controller to javafx.fxml;
}