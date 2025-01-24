package hr.java.restaurant.util;

import hr.java.restaurant.controller.ContractAddStep1Controller;
import hr.java.restaurant.controller.ContractAddStep2Controller;
import hr.java.restaurant.controller.ContractAddStep3Controller;
import hr.java.restaurant.enumeration.ContractType;
import hr.java.restaurant.main.RestaurantApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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

    public static void loadSceneContract(String fxmlFileName, String title, String name, String salary, LocalDate startDate, LocalDate endDate, ContractType contractType, List<String> files) {
        try {
            FXMLLoader fxmlLoader = FXMLLoaderHelper.fxmlFilePath(fxmlFileName + ".fxml");

            Scene scene = new Scene(fxmlLoader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

            if (fxmlFileName.equals("contractAddStep2")) {
                ContractAddStep2Controller controller = fxmlLoader.getController();
                controller.initialize(name, salary, startDate, endDate, contractType, files);
            } else if (fxmlFileName.equals("contractAddStep1")) {
                ContractAddStep1Controller controller = fxmlLoader.getController();
                controller.initialize(name, salary, startDate, endDate, contractType, files);
            } else if (fxmlFileName.equals("contractAddStep3")) {
                ContractAddStep3Controller controller = fxmlLoader.getController();
                controller.initialize(name, salary, startDate, endDate, contractType, files);
            }

            RestaurantApplication.getMainStage().setTitle(title);
            RestaurantApplication.getMainStage().setScene(scene);
            RestaurantApplication.getMainStage().show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.showErrorDialog("Error", "Error loading scene: " + fxmlFileName);
        }
    }
}
