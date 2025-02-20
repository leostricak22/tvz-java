package hr.javafx.coffe.caffee.javafxcaffee.controller;

import hr.javafx.coffe.caffee.javafxcaffee.model.Beverage;
import hr.javafx.coffe.caffee.javafxcaffee.repository.AbstractRepository;
import hr.javafx.coffe.caffee.javafxcaffee.repository.BeveragesDatabaseRepository;
import hr.javafx.coffe.caffee.javafxcaffee.thread.DisplayCheapestBeverageThread;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SearchBeveragesController {

    @FXML
    private TextField beverageNameTextField;

    @FXML
    private TextField beveragePriceFromTextField;

    @FXML
    private TextField beveragePriceToTextField;

    @FXML
    private TextField beverageAlcoholPercentageTextField;

    @FXML
    private TableView<Beverage> beverageTableView;

    @FXML
    private TableColumn<Beverage, String> beverageNameColumn;

    @FXML
    private TableColumn<Beverage, String> beveragePriceColumn;

    @FXML
    private TableColumn<Beverage, String> beverageAlcoholPercentageColumn;

    @FXML
    private TableColumn<Beverage, String> beverageIdColumn;

    @FXML
    private TableColumn<Beverage, String> beverageOriginColumn;

    @FXML
    private Label cheapestBeverageLabel;

    private AbstractRepository<Beverage> beveragesRepository = new BeveragesDatabaseRepository<>();

    public void initialize() {

        beverageNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        beveragePriceColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPrice())));

        beverageAlcoholPercentageColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().calculatePercentage())));

        beverageIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));

        beverageOriginColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getOrigin())));

        DisplayCheapestBeverageThread thread = new DisplayCheapestBeverageThread(beveragesRepository, cheapestBeverageLabel);
        //Thread runner = new Thread(thread);
        //runner.start();

        Timeline cheapestBeverageTimeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            Optional<Beverage> cheapestBeverage;

            if (beveragesRepository instanceof BeveragesDatabaseRepository bdr) {
                cheapestBeverage = bdr.findCheapestBeverage();

                if (cheapestBeverage.isPresent()) {
                    cheapestBeverageLabel.setText(
                            "Najjeftinije piće je: " + cheapestBeverage.get().getName() + " po cijeni " + cheapestBeverage.get().getPrice());
                }
            }
        }), new KeyFrame(Duration.seconds(1)));

        cheapestBeverageTimeline.setCycleCount(Animation.INDEFINITE);
        cheapestBeverageTimeline.play();
    }

    public void filterBeverages() {
        List<Beverage> beverageList = beveragesRepository.findAll();

        String beverageName = beverageNameTextField.getText();

        if (!beverageName.isEmpty()) {
            beverageList = beverageList.stream()
                    .filter(beverage -> beverage.getName().toLowerCase().contains(beverageName.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (!beveragePriceFromTextField.getText().isEmpty()) {
            BigDecimal beverageFromPrice = new BigDecimal(beveragePriceFromTextField.getText());
            beverageList = beverageList.stream()
                    .filter(beverage -> beverage.getPrice().compareTo(beverageFromPrice) >= 0)
                    .collect(Collectors.toList());
        }

        if (!beveragePriceToTextField.getText().isEmpty()) {
            BigDecimal beverageToPrice = new BigDecimal(beveragePriceToTextField.getText());
            beverageList = beverageList.stream()
                    .filter(beverage -> beverage.getPrice().compareTo(beverageToPrice) <= 0)
                    .collect(Collectors.toList());
        }

        String beverageAlcoholPercentageString = beverageAlcoholPercentageTextField.getText();

        if (!beverageAlcoholPercentageString.isEmpty()) {
            BigDecimal beverageAlcoholPercentage = new BigDecimal(beverageAlcoholPercentageTextField.getText());
            beverageList = beverageList.stream()
                    .filter(beverage -> beverage.calculatePercentage().compareTo(beverageAlcoholPercentage) <= 0)
                    .collect(Collectors.toList());
        }

        // observable list je povezana s eventima, pa se automatski ažurira
        ObservableList<Beverage> beverageObservableList =
                FXCollections.observableList(beverageList);

        beverageTableView.setItems(beverageObservableList);
    }
}