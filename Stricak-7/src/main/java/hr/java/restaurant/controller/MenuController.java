package hr.java.restaurant.controller;

import hr.java.restaurant.main.RestaurantApplication;
import hr.java.restaurant.util.FXMLLoaderHelper;
import hr.java.restaurant.util.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;

import java.io.IOException;

public class MenuController {

    public void showSearchScreen(ActionEvent event) throws IOException {
        MenuItem menuItem = (MenuItem) event.getSource();
        FXMLLoader fxmlLoader = FXMLLoaderHelper.fxmlFilePath(menuItem.getId() + ".fxml");

        Scene scene = new Scene(fxmlLoader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        RestaurantApplication.getMainStage().setTitle("Search");
        RestaurantApplication.getMainStage().setScene(scene);
        RestaurantApplication.getMainStage().show();
    }

}
