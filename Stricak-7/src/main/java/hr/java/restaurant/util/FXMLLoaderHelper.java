package hr.java.restaurant.util;

import javafx.fxml.FXMLLoader;

import java.net.URL;

import static hr.java.service.Constants.RELATIVE_FXML_PATH;

public class FXMLLoaderHelper {

    public static FXMLLoader fxmlFilePath(String fileName) {
        try {
            String filePath = String.format(RELATIVE_FXML_PATH+"%s", fileName);
            return new FXMLLoader(new URL(filePath));
        } catch (Exception e) {
            throw new RuntimeException("Error loading FXML file: " + fileName, e);
        }
    }
}
