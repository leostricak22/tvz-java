package hr.javafx.coffe.caffee.javafxcaffee.controller;

import hr.javafx.coffe.caffee.javafxcaffee.main.CoffeeShopApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

public class MenuController {

    public void showSearchBeveragesScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(new URL(
                "file:./src/main/resources/hr/javafx/coffe/caffee/javafxcaffee/searchBeveragesScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1065, 611);

        CoffeeShopApplication.getMainStage().setTitle("Pretraga pića");
        CoffeeShopApplication.getMainStage().setScene(scene);
        CoffeeShopApplication.getMainStage().show();
    }

    public void showAddNewBeverageScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(new URL(
                "file:./src/main/resources/hr/javafx/coffe/caffee/javafxcaffee/addNewBeverageScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1065, 611);

        CoffeeShopApplication.getMainStage().setTitle("Pretraga pića");
        CoffeeShopApplication.getMainStage().setScene(scene);
        CoffeeShopApplication.getMainStage().show();

    }
}
