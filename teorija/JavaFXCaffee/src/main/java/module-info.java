module hr.javafx.coffe.caffee.javafxcaffee {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.incubator.vector;
    requires java.sql;


    opens hr.javafx.coffe.caffee.javafxcaffee to javafx.fxml;
    exports hr.javafx.coffe.caffee.javafxcaffee.controller;
    opens hr.javafx.coffe.caffee.javafxcaffee.controller to javafx.fxml;
    exports hr.javafx.coffe.caffee.javafxcaffee.main;
    opens hr.javafx.coffe.caffee.javafxcaffee.main to javafx.fxml;
}