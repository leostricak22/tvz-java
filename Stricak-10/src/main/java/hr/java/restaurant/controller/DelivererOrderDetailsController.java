package hr.java.restaurant.controller;

import hr.java.restaurant.model.Deliverer;
import hr.java.restaurant.model.Order;
import hr.java.restaurant.util.AlertDialog;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.Set;

public class DelivererOrderDetailsController {

    @FXML
    private ScrollPane scrollPane;

    private Runnable onComplete;

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }

    public void initialize(Deliverer deliverer) {
        Set<Order> orders = deliverer.getWaitingOrders();

        VBox orderBox = new VBox(10);

        for (Order order : orders) {
            Label orderIdLabel = new Label("Order ID: " + order.getId());
            Button deliverButton = new Button("Deliver");
            Label timerLabel = new Label("10");
            timerLabel.idProperty().set("timerLabel" + order.getId());

            new Thread(() -> {
                for (int i = 10; i >= 0; i--) {
                    int finalI = i;
                    Platform.runLater(() -> timerLabel.setText(String.valueOf(finalI)));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Platform.runLater(() -> {
                    deliverButton.setDisable(true);
                });
            }).start();

            deliverButton.setOnAction(event -> {
                deliverButton.setDisable(true);
                deliverButton.setStyle("-fx-background-color: gray;");

                timerLabel.setVisible(false);
                deliverer.deliverInProcessOrder(order);

                new Thread(() -> {
                    int delay = 5 + (int) (Math.random() * 6);
                    for (int i = delay; i >= 0; i--) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Platform.runLater(() -> {
                        deliverButton.setDisable(true);
                        deliverer.deliverOrder(order);
                        AlertDialog.showInformationDialog("Order delivered", "Order with ID " + order.getId() + " has been delivered.");
                    });
                }).start();
            });

            orderBox.getChildren().addAll(orderIdLabel, deliverButton, timerLabel);
        }

        scrollPane.setContent(orderBox);
    }
}
