package hr.java.restaurant.util;

import hr.java.restaurant.controller.MultipleCheckBoxSelectController;
import hr.java.restaurant.main.RestaurantApplication;
import hr.java.restaurant.model.Entity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MultipleCheckBoxSelectUtil {
    public static <T extends Entity> Set<T> openWindow(Set<T> items, Set<T> previouslySelectedItems) {
        Set<T> selectedItems = new HashSet<>();

        try {
            FXMLLoader fxmlLoader = FXMLLoaderHelper.fxmlFilePath("multipleCheckBoxSelect.fxml");
            Scene scene = new Scene(fxmlLoader.load(), 300, 250);

            MultipleCheckBoxSelectController<T> controller = fxmlLoader.getController();

            controller.initialize(new ArrayList<>(items), new ArrayList<>(previouslySelectedItems));

            controller.setOnConfirmCallback(selectedItems::addAll);

            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Multi-Select Window");
            secondaryStage.setScene(scene);

            secondaryStage.initModality(Modality.WINDOW_MODAL);
            secondaryStage.initOwner(RestaurantApplication.getMainStage());

            secondaryStage.showAndWait();
        } catch (IOException e) {
            AlertDialog.showErrorDialog("Failed to open a new window", "Failed to open a new window");
        }

        return selectedItems;
    }
}
