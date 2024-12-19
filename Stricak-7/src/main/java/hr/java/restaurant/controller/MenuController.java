package hr.java.restaurant.controller;

import hr.java.restaurant.main.RestaurantApplication;
import hr.java.restaurant.util.FXMLLoaderHelper;
import hr.java.service.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;

import java.io.IOException;

public class MenuController {
    @FXML
    MenuItem categorySearchMenuItem = new MenuItem("Pretraga kategorija");

    public void showCategorySearchScreen() throws IOException {
        FXMLLoader fxmlLoader = FXMLLoaderHelper.fxmlFilePath("categorySearch.fxml");

        Scene scene = new Scene(fxmlLoader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        RestaurantApplication.getMainStage().setTitle("Pretraga kategorija");
        RestaurantApplication.getMainStage().setScene(scene);
        RestaurantApplication.getMainStage().show();
    }
}
