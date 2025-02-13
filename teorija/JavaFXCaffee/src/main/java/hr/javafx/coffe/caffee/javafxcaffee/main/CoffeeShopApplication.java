package hr.javafx.coffe.caffee.javafxcaffee.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class CoffeeShopApplication extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(new URL(
                "file:./src/main/resources/hr/javafx/coffe/caffee/javafxcaffee/welcomePageScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1065, 611);

        mainStage = stage;

        stage.setTitle("Pretraga pića");
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