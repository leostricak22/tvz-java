package hr.java.restaurant.util;

import hr.java.restaurant.controller.MultipleCheckBoxSelectController;
import hr.java.restaurant.main.RestaurantApplication;
import hr.java.restaurant.model.Entity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MultipleCheckBoxSelectUtil {
    public static <T extends Entity> List<T> openWindow(Set<T> items, List<T> previouslySelectedItems) {
        List<T> selectedItems = new ArrayList<>();

        try {
            FXMLLoader fxmlLoader = FXMLLoaderHelper.fxmlFilePath("multipleCheckBoxSelect.fxml");
            Scene scene = new Scene(fxmlLoader.load(), 300, 250);

            MultipleCheckBoxSelectController<T> controller = fxmlLoader.getController();

            controller.initialize(new ArrayList<>(items), previouslySelectedItems);

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
