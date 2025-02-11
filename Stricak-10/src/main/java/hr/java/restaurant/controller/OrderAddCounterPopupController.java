package hr.java.restaurant.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class OrderAddCounterPopupController {

    @FXML
    private Label countdownLabel;

    private Stage popupStage;
    private Runnable onComplete;

    public void setPopupStage(Stage stage) {
        this.popupStage = stage;
    }

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }

    public void startCountdown() {
        Timeline countdown = new Timeline();
        for (int i = 5; i >= 0; i--) {
            int finalI = i;
            countdown.getKeyFrames().add(new KeyFrame(Duration.seconds(5 - i), e -> countdownLabel.setText(String.valueOf(finalI))));
        }

        countdown.setOnFinished(e -> {
            popupStage.close();
            if (onComplete != null) onComplete.run();
        });

        countdown.play();
    }
}
