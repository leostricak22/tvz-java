package hr.java.restaurant.util;

import hr.java.restaurant.main.RestaurantApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {
    public static void loadScene(String fxmlFileName, String title) {
        try {
            FXMLLoader fxmlLoader = FXMLLoaderHelper.fxmlFilePath(fxmlFileName + ".fxml");

            Scene scene = new Scene(fxmlLoader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

            RestaurantApplication.getMainStage().setTitle(title);
            RestaurantApplication.getMainStage().setScene(scene);
            RestaurantApplication.getMainStage().show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.showErrorDialog("Error", "Error loading scene: " + fxmlFileName);
        }
    }
}
