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
        // ako ne postoji fxml datoteka, baca iznimku IOException

        // učitava FXML datoteku
        // moguće i fxmlLoader = FXMLLoader.load(CoffeShopApplication.getClass().getResource("/hr/javafx/coffe/caffee/javafxcaffee/welcomePageScreen.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(new URL(
                "file:./src/main/resources/hr/javafx/coffe/caffee/javafxcaffee/welcomePageScreen.fxml"));

        // postavlja scenu
        // moguće i bez širine i visine
        Scene scene = new Scene(fxmlLoader.load(), 1065, 611);

        mainStage = stage;

        stage.setTitle("Pretraga pića");

        // postavlja scenu na stage i prikazuje stage
        stage.setScene(scene);
        stage.show();
    }

    // pokreće aplikaciju, poziva se launch metoda iz Application klase
    public static void main(String[] args) {
        launch();
    }

    public static Stage getMainStage() {
        return mainStage;
    }
}