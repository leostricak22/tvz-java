package hr.java.restaurant.controller;

import hr.java.restaurant.main.RestaurantApplication;
import hr.java.restaurant.model.Chef;
import hr.java.restaurant.model.Person;
import hr.java.restaurant.util.FXMLLoaderHelper;
import hr.java.restaurant.util.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

    public void showAddScreen(ActionEvent event) throws IOException {
        MenuItem menuItem = (MenuItem) event.getSource();
        FXMLLoader fxmlLoader = FXMLLoaderHelper.fxmlFilePath(menuItem.getId() + ".fxml");

        Scene scene = new Scene(fxmlLoader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        RestaurantApplication.getMainStage().setTitle("Add");
        RestaurantApplication.getMainStage().setScene(scene);
        RestaurantApplication.getMainStage().show();
    }

    public void showAddPersonScreen(ActionEvent event) throws IOException {
        MenuItem menuItem = (MenuItem) event.getSource();
        FXMLLoader fxmlLoader = FXMLLoaderHelper.fxmlFilePath("personAdd.fxml");

        String title = "Add new Chef";
        PersonAddController<?> controller = new ChefAddController();

        if (menuItem.getId().equals("waiterAdd")) {
            controller = new WaiterAddController();
            title = "Add new Waiter";
        } else if (menuItem.getId().equals("delivererAdd")) {
            controller = new DelivererAddController();
            title = "Add new Deliverer";
        }

        PersonAddController<?> pac = controller;
        pac.setTitle(title);

        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        RestaurantApplication.getMainStage().setTitle("Add");
        RestaurantApplication.getMainStage().setScene(scene);
        RestaurantApplication.getMainStage().show();
    }
}
