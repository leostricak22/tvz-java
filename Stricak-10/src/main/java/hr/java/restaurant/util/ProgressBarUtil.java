package hr.java.restaurant.util;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;

public class ProgressBarUtil {

    private ProgressBar progressBar;
    private StackPane progressBarStackPane;
    private final AnchorPane mainPane;

    public ProgressBarUtil(AnchorPane mainPane) {
        this.mainPane = mainPane;
        create();
    }

    public void create() {
        progressBarStackPane = new StackPane();
        progressBarStackPane.setPrefSize(Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        progressBarStackPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        progressBar = new ProgressBar();
        progressBar.setPrefSize(200, 20);
        progressBar.setProgress(0);
        progressBarStackPane.getChildren().add(progressBar);
        mainPane.getChildren().add(progressBarStackPane);
    }

    public void update(BigDecimal progress) {
        progressBar.setProgress(progress.doubleValue());
    }

    public void remove() {
        mainPane.getChildren().remove(progressBarStackPane);
    }
}
