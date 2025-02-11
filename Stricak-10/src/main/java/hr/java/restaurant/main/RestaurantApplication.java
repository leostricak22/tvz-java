package hr.java.restaurant.main;
import hr.java.restaurant.util.FXMLLoaderHelper;
import hr.java.restaurant.util.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RestaurantApplication extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = FXMLLoaderHelper.fxmlFilePath("firstScreen.fxml");
        Scene scene = new Scene(fxmlLoader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        mainStage = stage;
        //HighestPaidPersonThread.start();

        stage.setTitle("Pretraga kategorija");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getMainStage() {
        return mainStage;
    }
}
